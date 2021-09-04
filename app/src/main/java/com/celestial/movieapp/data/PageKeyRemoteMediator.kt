package com.celestial.movieapp.data

import androidx.paging.*
import androidx.room.withTransaction
import com.celestial.movieapp.data.local.MoviesDatabase
import com.celestial.movieapp.data.local.RemoteKeyDao
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.data.model.UpcomingResponse
import com.celestial.movieapp.data.network.api.MoviesAPI
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyRemoteMediator (
    private val db: MoviesDatabase,
    private val api: MoviesAPI
        ): RemoteMediator<Int, MovieModel>(){

    private val movieDao = db.moviesDao()
    private val remoteKeyDao = db.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieModel>
    ): MediatorResult {
       try{
           val loadKey = when(loadType){
               LoadType.REFRESH -> 1
               LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
               LoadType.APPEND -> {
                   val remoteKey = db.withTransaction {
                       remoteKeyDao.remoteKeyByPage()
                   }
                   if(remoteKey.page == remoteKey.totalPages){
                       return MediatorResult.Success(endOfPaginationReached = true)
                   }
                    val num = remoteKey.page+1
                    num
               }
           }

           val data = api.getUpcomingMovies(
               page = loadKey
           )

           val moviesList: UpcomingResponse?  = data.body()

           val let = moviesList?.let {
               db.withTransaction {
                   if (loadType == LoadType.REFRESH) {
                       movieDao.deleteAllMovies()
                       remoteKeyDao.deleteByKey()
                   }

                   remoteKeyDao.insert(it.commonResponse)
                   movieDao.insertAllMovies(it.result)

               }

               return MediatorResult.Success(endOfPaginationReached = it.result.isEmpty())
           }

           return MediatorResult.Success(endOfPaginationReached = true)
       }catch (e: IOException){
           return MediatorResult.Error(e)
       }catch (e: HttpException){
           return MediatorResult.Error(e)
       }
    }

}