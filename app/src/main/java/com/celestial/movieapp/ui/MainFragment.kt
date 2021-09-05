package com.celestial.movieapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.celestial.movieapp.MainActivity
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.data.others.LoadingState
import com.celestial.movieapp.data.others.Resource
import com.celestial.movieapp.data.others.Status
import com.celestial.movieapp.databinding.FragmentMainBinding
import com.celestial.movieapp.ui.adapter.MovieAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


private val TAG = MainFragment::class.java.name

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    lateinit var moviesViewModel: MoviesViewModel

    lateinit var upcomingMovieAdapter: MovieAdapter

    lateinit var upcomingRV: RecyclerView

    lateinit var popularRV: RecyclerView

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var upcomingProgress: ProgressBar

    lateinit var popularProgress: ProgressBar

    lateinit var popularMovieAdapter: MovieAdapter

    lateinit var navController: NavController

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
        observeMovies()

        return view
    }

    private fun observeMovies() {
        lifecycleScope.launch {
            moviesViewModel.fetchUpComingMovies(LoadingState.REFRESH).observe(viewLifecycleOwner, upcomingDataObserver)
        }

        lifecycleScope.launch{
            moviesViewModel.fetchPopularMovies(LoadingState.REFRESH).observe(viewLifecycleOwner, popularDataObserver)
        }

        swipeRefreshLayout.setOnRefreshListener {
            refreshUpComingMovies()
            refreshPopularMovies()
        }
    }

    private fun refreshUpComingMovies(){
        moviesViewModel.fetchUpComingMovies(LoadingState.REFRESH).observe(viewLifecycleOwner,upcomingDataObserver)
        upcomingMovieAdapter.clearMovies()
    }

    private fun refreshPopularMovies(){
        moviesViewModel.fetchPopularMovies(LoadingState.REFRESH).observe(viewLifecycleOwner, popularDataObserver)
        popularMovieAdapter.clearMovies()
    }

    val upcomingDataObserver = Observer<Resource<List<MovieModel>>> {
        when(it.status){
            Status.LOADING ->{Log.d(TAG,"LOADING")
            upcomingProgress.visibility = View.VISIBLE
            }
            Status.SUCCESS ->{
                upcomingProgress.visibility = View.INVISIBLE
                swipeRefreshLayout.isRefreshing = false
                Log.d(TAG,"SUCCESS"+ it.data.toString())
                it.data?.let {  upcomingMovieAdapter.addMovies(it) }
            }
            Status.ERROR ->{
                upcomingProgress.visibility = View.INVISIBLE
                swipeRefreshLayout.isRefreshing = false
                it.data?.let {
                    upcomingMovieAdapter.addMovies(it)
                }
                Snackbar.make(binding.root,
                it.message?:"Unknown error occured",
                Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry") {
                        refreshUpComingMovies()
                    }
                    .show()
                Log.d(TAG,"ERRoR")}
        }
    }

    val popularDataObserver = Observer<Resource<List<MovieModel>>> {
        when(it.status){
            Status.LOADING ->{Log.d(TAG,"LOADING")
                popularProgress.visibility = View.VISIBLE
            }
            Status.SUCCESS ->{
                popularProgress.visibility = View.INVISIBLE
                swipeRefreshLayout.isRefreshing = false
                Log.d(TAG,"POPULAR SUCCESS"+ it.data.toString())
                it.data?.let {  popularMovieAdapter.addMovies(it) }
            }
            Status.ERROR ->{
                popularProgress.visibility = View.INVISIBLE
                swipeRefreshLayout.isRefreshing = false
                it.data?.let {
                    popularMovieAdapter.addMovies(it)
                }
                Snackbar.make(binding.root,
                    it.message?:"Unknown error occured",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry") {
                        refreshPopularMovies()
                    }
                    .show()
                Log.d(TAG,"ERRoR")}
        }
    }


    private fun setUpView() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        upcomingProgress = binding.upcomingMovieProgress
        popularProgress = binding.popularMovieProgress

        upcomingMovieAdapter = MovieAdapter(requireContext(),favInterface, itemClick)
        popularMovieAdapter = MovieAdapter(requireContext(),favInterface, itemClick)

        upcomingRV = binding.upcomingRcyView
        popularRV = binding.popularRcyView

        upcomingRV.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = upcomingMovieAdapter
        }

        popularRV.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularMovieAdapter
        }

        upcomingRV.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = (upcomingRV.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(dx>0){

                    if(lastVisibleItem == upcomingRV?.adapter?.itemCount?.minus(1)?: return){
                        Log.d(TAG,"PAGI ${!MoviesViewModel.isFetchingUpcomingMovies} :: ${!MoviesViewModel.isCachedDataLoadedForUpcomingMovies}")
                    }

                    if(!MoviesViewModel.isFetchingUpcomingMovies && !MoviesViewModel.isCachedDataLoadedForUpcomingMovies &&
                        lastVisibleItem == upcomingRV?.adapter?.itemCount?.minus(1) ?: return){
                        moviesViewModel.fetchUpComingMovies(LoadingState.NEXT).observe(viewLifecycleOwner, upcomingDataObserver)
                        Log.d(TAG,"PAGINATION CALLED")
                    }
                }
            }
        })

        popularRV.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = (popularRV.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(dx>0){

                    if(lastVisibleItem == popularRV?.adapter?.itemCount?.minus(1)?: return){
                        Log.d(TAG,"PAGI ${!MoviesViewModel.isFetchingPopularMovies} :: ${!MoviesViewModel.isCachedDataLoadedForPopularMovies}")
                    }

                    if(!MoviesViewModel.isFetchingPopularMovies && !MoviesViewModel.isCachedDataLoadedForPopularMovies &&
                        lastVisibleItem == popularRV?.adapter?.itemCount?.minus(1) ?: return){
                        moviesViewModel.fetchPopularMovies(LoadingState.NEXT).observe(viewLifecycleOwner, popularDataObserver)
                        Log.d(TAG,"PAGINATION CALLED POPULAR")
                    }
                }
            }
        })



    }

    val favInterface = object :FavInterface{
        override fun setFav(movie: MovieModel) {
            Log.d(TAG,"setFav Called"+movie.toString())
            moviesViewModel.updateMovieAsFavourite(movie)
        }
    }

    val itemClick = object :ItemClickCallback{
        override fun itemClick(movie: MovieModel) {
            MoviesViewModel.detailMovie = movie
            (activity as MainActivity).navigateToDetail()
        }
    }


    interface FavInterface{
        fun setFav(movie: MovieModel)
    }

    interface  ItemClickCallback{
        fun itemClick(movie: MovieModel)
    }



}


