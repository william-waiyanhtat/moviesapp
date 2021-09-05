package com.celestial.movieapp.ui

import android.util.Log
import androidx.lifecycle.*
import com.celestial.movieapp.data.MoviesRepository
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.data.others.LoadingState
import com.celestial.movieapp.data.others.RepoState
import com.celestial.movieapp.data.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {
    val TAG = MoviesViewModel::class.simpleName
   // val movies = moviesRepository.observeAllMovies()

    val upcomingMovieList = MutableLiveData<Resource<List<MovieModel>>>()

    val popularMovieList = MutableLiveData<Resource<List<MovieModel>>>()

   companion object{
       var isFetchingUpcomingMovies: Boolean = false
       var isFetchingPopularMovies: Boolean = false

       var isCachedDataLoadedForUpcomingMovies = false
       var isCachedDataLoadedForPopularMovies = false

       var detailMovie: MovieModel? = null
   }

    fun updateMovieAsFavourite(movie: MovieModel){
        viewModelScope.launch {
            moviesRepository.setFavourite(movie)
        }

    }

    fun fetchUpComingMovies(loadingState: LoadingState): LiveData<Resource<List<MovieModel>>>{
        isFetchingUpcomingMovies = true

        upcomingMovieList.postValue(Resource.loading(null))
        viewModelScope.launch {
            moviesRepository.fetchUpcomingMovies(loadingState, object : RepoState{
                override fun success(movies: List<MovieModel>) {
                    upcomingMovieList.postValue(Resource.success(movies))
                    isFetchingUpcomingMovies = false
                    isCachedDataLoadedForUpcomingMovies = false
                    Log.d(TAG, "SUCCESS :${movies.size}")
                }

                override fun loading(msg: String) {
                    upcomingMovieList.postValue(Resource.loading(null))
                    isFetchingUpcomingMovies = false
                }

                override fun failed(movie: List<MovieModel>?,errMsg: String) {
                   movie?.let {
                       upcomingMovieList.postValue(Resource.error(errMsg,movie))
                       isCachedDataLoadedForUpcomingMovies = true
                   }

                    isFetchingUpcomingMovies = false
                    upcomingMovieList.postValue(Resource.error(errMsg,movie))

                }
            })
        }
        return upcomingMovieList
    }

    fun fetchPopularMovies(loadingState: LoadingState): LiveData<Resource<List<MovieModel>>>{
        isFetchingPopularMovies = true

        popularMovieList.postValue(Resource.loading(null))
        viewModelScope.launch {
            moviesRepository.fetchPopularMovies(loadingState, object : RepoState{
                override fun success(movies: List<MovieModel>) {
                    popularMovieList.postValue(Resource.success(movies))
                    isFetchingPopularMovies = false
                    isCachedDataLoadedForPopularMovies = false
                    Log.d(TAG, "SUCCESS :${movies.size}")
                }

                override fun loading(msg: String) {
                    popularMovieList.postValue(Resource.loading(null))
                    isFetchingPopularMovies = false
                }

                override fun failed(movie: List<MovieModel>?,errMsg: String) {
                    movie?.let {
                        popularMovieList.postValue(Resource.error(errMsg,movie))
                        isCachedDataLoadedForPopularMovies = true
                    }
                    isFetchingPopularMovies = false
                    popularMovieList.postValue(Resource.error(errMsg,movie))

                }
            })
        }
        return popularMovieList
    }



}