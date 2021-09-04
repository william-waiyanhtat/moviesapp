package com.celestial.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.databinding.PopularItemBinding
import com.celestial.movieapp.databinding.UpcomingItemBinding

class UpcomingAdapter: PagingDataAdapter<MovieModel, UpcomingAdapter.MovieItemViewHolder>(DIFF_CALLBACK) {

    companion object{
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding = UpcomingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        with(holder){
            getItem(position)?.let{
                binding.tvPopular.text = it.title
            }
        }
    }


    inner class MovieItemViewHolder( val binding: UpcomingItemBinding): RecyclerView.ViewHolder(binding.root)
}