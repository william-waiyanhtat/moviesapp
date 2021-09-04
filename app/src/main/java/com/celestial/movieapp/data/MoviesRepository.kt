package com.celestial.movieapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.celestial.movieapp.data.local.MoviesDao
import com.celestial.movieapp.data.local.MoviesDatabase
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.data.network.api.MoviesAPI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


val TAG = MoviesRepository::class.java.name

class MoviesRepository @Inject constructor(
    private val moviesDatabase: MoviesDatabase,
    private val moviesAPI: MoviesAPI
) {
    @ExperimentalPagingApi
    fun fetchUpcomingMoviesList(): Flow<PagingData<MovieModel>>{
        return Pager(
            PagingConfig(
                pageSize = 20, enablePlaceholders = false, prefetchDistance = 1),
                remoteMediator = PageKeyRemoteMediator(moviesDatabase,moviesAPI),
                pagingSourceFactory = {moviesDatabase.moviesDao().readAllUpcomingMovies()}
            ).flow

    }

}