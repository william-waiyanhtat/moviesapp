package com.celestial.movieapp.data.others

import com.celestial.movieapp.data.model.MovieModel

interface RepoState {

    fun success(movies: List<MovieModel>)
    fun loading(msg: String)
    fun failed(movies: List<MovieModel>?, errMsg: String)
}