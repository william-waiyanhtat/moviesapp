package com.celestial.movieapp.ui

import androidx.lifecycle.ViewModel
import com.celestial.movieapp.data.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel()