package com.celestial.movieapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celestial.movieapp.R
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.databinding.FragmentMainBinding
import com.celestial.movieapp.ui.adapter.MoviesLoadingAdapter
import com.celestial.movieapp.ui.adapter.PopularAdapter
import com.celestial.movieapp.ui.adapter.UpcomingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private val TAG = MainFragment::class.java.name

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private lateinit var popularAdapter: PopularAdapter

    lateinit var moviesViewModel: MoviesViewModel

    lateinit var adapter: UpcomingAdapter

    lateinit var upcomingRV: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        setUpView()
        fetchPosts()

        return view
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun fetchPosts() {
        lifecycleScope.launch {
            moviesViewModel.fetchUpcomingMovies().collectLatest {
                pagingData -> adapter.submitData(pagingData)
            }

        }
    }

    private fun setUpView() {
        adapter = UpcomingAdapter(requireContext(), favInterface)
        upcomingRV = binding.upcomingRcyView
        upcomingRV.apply {
            layoutManager = LinearLayoutManager(context)
        }



        upcomingRV.adapter = adapter.also {
            it.withLoadStateHeaderAndFooter(
                header = MoviesLoadingAdapter{it.retry()},
                footer = MoviesLoadingAdapter{it.retry()}
            )
        }
    }

    val favInterface = object :FavInterface{
        override fun setFav(movie: MovieModel) {
            Log.d(TAG,"setFav Called"+movie.toString())
            moviesViewModel.updateMovieAsFavourite(movie)
        }
    }


    interface FavInterface{
        fun setFav(movie: MovieModel)
    }

}