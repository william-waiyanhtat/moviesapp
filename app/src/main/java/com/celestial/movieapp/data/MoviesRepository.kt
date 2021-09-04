package com.celestial.movieapp.data

import com.celestial.movieapp.data.local.MoviesDao
import com.celestial.movieapp.data.network.api.MoviesAPI
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val moviesAPI: MoviesAPI
) {


}