package com.celestial.movieapp.data

import com.celestial.movieapp.data.model.MovieModel

interface DefaultRepository {

    suspend fun setAsFavourite(movies: MovieModel)


}