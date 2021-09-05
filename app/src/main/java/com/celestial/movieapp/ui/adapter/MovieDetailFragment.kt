package com.celestial.movieapp.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.celestial.movieapp.R
import com.celestial.movieapp.databinding.FragmentMainBinding
import com.celestial.movieapp.databinding.FragmentMovieDetailBinding
import com.celestial.movieapp.ui.MoviesViewModel


class MovieDetailFragment : Fragment() {

    lateinit var moviesViewModel: MoviesViewModel
    private var _binding: FragmentMovieDetailBinding? = null

    private val binding get() = _binding!!

    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         glide = Glide.with(requireActivity().applicationContext).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
        )

        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        bindData()
        // Inflate the layout for this fragment
        return view
    }

    private fun bindData() {
        val movie = MoviesViewModel.detailMovie
        movie?.let{
            glide.load("https://image.tmdb.org/t/p/w500" + it.backdropPath)
                .into(binding.imageView)
            binding.tvMovieTitle.text = it.title
            binding.tvReleaseDate.text = it.releaseDate
            binding.tvOverview.text = it.overview
            binding.tvRating.text = "Average Rating : ${it.voteAverage}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MoviesViewModel.detailMovie = null
    }

}