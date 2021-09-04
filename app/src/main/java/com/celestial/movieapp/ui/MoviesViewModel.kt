package com.celestial.movieapp.ui

import androidx.lifecycle.*
import androidx.paging.*
import com.celestial.movieapp.data.MoviesRepository
import com.celestial.movieapp.data.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {


    @ExperimentalPagingApi
    fun fetchUpcomingMovies(): Flow<PagingData<MovieModel>>{
        return moviesRepository.fetchUpcomingMoviesList().cachedIn(viewModelScope)
    }

    @ExperimentalPagingApi
    fun fetchPopularMovies(): Flow<PagingData<MovieModel>>{
        return moviesRepository.fetchUpcomingMoviesList().cachedIn(viewModelScope)
    }

    fun updateMovieAsFavourite(movie: MovieModel){
        viewModelScope.launch {
            moviesRepository.setFavourite(movie)
        }

    }

}