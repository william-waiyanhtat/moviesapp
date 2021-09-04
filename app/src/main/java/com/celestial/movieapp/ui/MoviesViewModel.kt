package com.celestial.movieapp.ui

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.celestial.movieapp.data.MoviesRepository
import com.celestial.movieapp.data.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {

    fun tryAPICall(): LiveData<Int>{

        var ld = MutableLiveData<Int>()
        viewModelScope.launch {
         //   moviesRepository.fetchPopularMoviesList()
            ld.postValue(1)

        }
      return ld
    }

//    fun getUpComingMovies(): Flow<PagingData<MovieModel>>{
//       // return Pager(config = )
//    }
}