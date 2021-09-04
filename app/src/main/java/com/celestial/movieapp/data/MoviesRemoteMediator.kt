package com.celestial.movieapp.data

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.celestial.movieapp.data.local.MoviesDatabase
import com.celestial.movieapp.data.local.RemoteKeyDao
import com.celestial.movieapp.data.model.CommonResponse
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.data.model.UpcomingResponse
import com.celestial.movieapp.data.network.api.MoviesAPI
import retrofit2.HttpException
import java.io.IOException




@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator (
    private val db: MoviesDatabase,
    private val api: MoviesAPI,
    private val isUpcomingMovie: Boolean
        ): RemoteMediator<Int, MovieModel>(){

    private val movieDao = db.moviesDao()
    private val remoteKeyDao = db.remoteKeyDao()


    val TAG: String? = MoviesRemoteMediator::class.simpleName

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieModel>
    ): MediatorResult {
       return try{
           val loadKey = when(loadType){
               LoadType.REFRESH -> {
                   Log.d(TAG,"REFRESH")
                   null
               }
               LoadType.PREPEND -> {
                   Log.d(TAG,"PREPEND")
                   return MediatorResult.Success(endOfPaginationReached = true)
               }
               LoadType.APPEND -> {

                   Log.d(TAG,"APPEND LIST")
                   state.lastItemOrNull()?: return MediatorResult.Success(endOfPaginationReached = true)

                   Log.d(TAG,"APPEND LIST->RemoteKey")
                   val remoteKey = db.withTransaction {
                       remoteKeyDao.remoteKeyByPage().firstOrNull()
                   }
                   remoteKey
               }
           }

           var pageNo = 1

           if (loadKey != null) {
               if(loadKey.page<loadKey.totalPages){
                   pageNo = loadKey.page+1
               }else{
                   return MediatorResult.Success(endOfPaginationReached = true)
               }
           }

           val data = if(isUpcomingMovie){
               api.getUpcomingMovies(
               page = pageNo)
           }else{
               api.getPopularMovies(
                   page = pageNo
               )
           }

           val moviesList: CommonResponse?  = data.body()

//           val let = moviesList?.let {
//               db.withTransaction {
//                   if (loadType == LoadType.REFRESH) {
//                       movieDao.deleteAllMovies()
//                       remoteKeyDao.deleteByKey()
//                   }
//
//                   Log.d(TAG, it.toString())
//
//                   remoteKeyDao.insert(it)
//                   it?.result?.let { it1 ->
//
//                       it1.map { it ->
//                           if(isUpcomingMovie)
//                           it.isUpcoming = true
//                           else
//                           it.isPopular = true
//
//                       }
//                       movieDao.insertAllMovies(it1) }
//
//               }
//
//               return MediatorResult.Success(endOfPaginationReached = it.result?.isEmpty()!!)
//           }
            val items = moviesList?.result
            val movies = items?.map {
                    it ->
                           if(isUpcomingMovie)
                           it.isUpcoming = true
                           else
                               it.isPopular = true
                    it
            }

           db.withTransaction {
               moviesList?.let { remoteKeyDao.insert(it) }
               movieDao.insertAllMovies(movies!!)
           }


           return MediatorResult.Success(endOfPaginationReached = pageNo==data.body()!!.totalPages)
       }catch (e: IOException){
           return MediatorResult.Error(e)
       }catch (e: HttpException){
           return MediatorResult.Error(e)
       }
    }

}