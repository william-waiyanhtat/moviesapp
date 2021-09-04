package com.celestial.movieapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.celestial.movieapp.R
import com.celestial.movieapp.databinding.FragmentMainBinding
import com.celestial.movieapp.ui.adapter.PopularAdapter


private val TAG = MainFragment::class.java.name

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? =null

    private val binding get() = _binding!!

    private lateinit var popularAdapter: PopularAdapter

    lateinit var moviesViewModel: MoviesViewModel

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
        setUpAdapter()
        setUpRecylerView()
        observeData()


        return view
    }

    private fun observeData() {
        moviesViewModel?.let{
            it.tryAPICall().observe(viewLifecycleOwner, Observer {
                it-> run{
                    Log.d(TAG,"Item Come back")

            }

            })
        }
    }

    private fun setUpAdapter() {
        popularAdapter = PopularAdapter()
    }

    private fun setUpRecylerView() {
       binding.upcomingRcyView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
       binding.upcomingRcyView.adapter = popularAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment()
    }


}