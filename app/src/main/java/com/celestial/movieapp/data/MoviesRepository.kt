package com.celestial.movieapp.data

import android.content.Context
import android.util.Log
import com.celestial.movieapp.data.local.MoviesDatabase
import com.celestial.movieapp.data.model.CommonResponse
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.data.network.api.MoviesAPI
import com.celestial.movieapp.data.others.LoadingState
import com.celestial.movieapp.data.others.NetworkUitls
import com.celestial.movieapp.data.others.RepoState
import java.lang.Exception
import javax.inject.Inject


val TAG = MoviesRepository::class.java.name

class MoviesRepository @Inject constructor(
    private val moviesDatabase: MoviesDatabase,
    private val moviesAPI: MoviesAPI,
    private val context: Context
) {
     var upComingPageNo: Int = 0
     var popularPageNo: Int = 0

    suspend fun setFavourite(movie: MovieModel) {
        moviesDatabase.moviesDao().updateMovie(movie)
    }

    suspend fun fetchUpcomingMovies(loadingState: LoadingState, repoState: RepoState) {
        Log.d(TAG, "Page No:$upComingPageNo")
        if (NetworkUitls.isOnline(context)) {
            when (loadingState) {
                LoadingState.REFRESH -> {
                    upComingPageNo = 1
                    Log.d(TAG, "REFRESH")
                }
                LoadingState.NEXT -> {
                    upComingPageNo = moviesDatabase.remoteKeyDao().getUpcomingRemoteKeyByPage()?.firstOrNull()?.page!!
                    upComingPageNo += 1
                    Log.d(TAG, "NEXT")
                }
            }
            try {
                val response = moviesAPI.getUpcomingMovies(page = upComingPageNo)
                if (response.isSuccessful) {
                    val data: CommonResponse? = response.body()
                    val moviesList = data?.result
                    val page = data?.page
                    val totalPage = data?.totalPages

                    moviesList?.map {
                        it.time = System.nanoTime()
                        it.page = data.page
                        it.isUpcoming = true
                    }
                    moviesList?.let {
                        moviesDatabase.moviesDao().insertAllMovies(it)
                        data.isUpComing = true
                        moviesDatabase.remoteKeyDao().insert(data)
                        repoState.success(moviesDatabase.moviesDao().readUpcomingMovieListByPage(data.page))
                    }
                } else {
                    repoState.failed(null, response.errorBody().toString())
                }
            } catch (e: Exception) {
                val cache = moviesDatabase.moviesDao().readAllUpcomingMovieList()
                Log.d(TAG, "INTERNET ERROR")
                repoState.failed(cache, "Connection Timeout")
            }

        } else {
            val cache = moviesDatabase.moviesDao().readAllUpcomingMovieList()
            repoState.failed(cache, "No Internet Connection")
        }

    }

    suspend fun fetchPopularMovies(loadingState: LoadingState, repoState: RepoState){
        if (NetworkUitls.isOnline(context)) {
            when (loadingState) {
                LoadingState.REFRESH -> {
                    popularPageNo = 1
                    Log.d(TAG, "REFRESH")
                }
                LoadingState.NEXT -> {
                    popularPageNo = moviesDatabase.remoteKeyDao().getPopularRemoteKeyByPage()?.firstOrNull()?.page!!
                    popularPageNo += 1
                    Log.d(TAG, "NEXT")
                }
            }
            try {
                val response = moviesAPI.getPopularMovies(page = popularPageNo)
                if (response.isSuccessful) {
                    val data: CommonResponse? = response.body()
                    val moviesList = data?.result
                    val page = data?.page
                    val totalPage = data?.totalPages

                    moviesList?.map {
                        it.time = System.nanoTime()
                        it.page = data.page
                    }
                    moviesList?.let {
                        moviesDatabase.moviesDao().insertAllMovies(it)
                        moviesDatabase.remoteKeyDao().insert(data)
                        repoState.success(moviesDatabase.moviesDao().readPopularMovieListByPage(data.page))
                    }
                } else {
                    repoState.failed(null, response.errorBody().toString())
                }
            } catch (e: Exception) {
                val cache = moviesDatabase.moviesDao().readAllPopularMovieList()
                Log.d(TAG, "INTERNET ERROR")
                repoState.failed(cache, "Connection Timeout")
            }

        } else {
            val cache = moviesDatabase.moviesDao().readAllPopularMovieList()
            repoState.failed(cache, "No Internet Connection")
        }
    }


}