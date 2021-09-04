package com.celestial.movieapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.celestial.movieapp.data.local.MoviesDao
import com.celestial.movieapp.data.local.MoviesDatabase
import com.celestial.movieapp.data.network.api.MoviesAPI
import javax.inject.Inject


val TAG = MoviesRepository::class.java.name

class MoviesRepository @Inject constructor(
    private val moviesDatabase: MoviesDatabase,
    private val moviesAPI: MoviesAPI
) {

    @OptIn(ExperimentalPagingApi::class)
    fun fetchUpcomingMoviesList() = Pager(
        config = PagingConfig(20),
        remoteMediator = PageKeyRemoteMediator(moviesDatabase, moviesAPI)
    ){
        moviesDatabase.moviesDao().readAllUpcomingMovies()
    }


}