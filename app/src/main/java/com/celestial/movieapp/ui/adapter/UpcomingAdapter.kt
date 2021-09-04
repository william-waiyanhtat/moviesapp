package com.celestial.movieapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.celestial.movieapp.R
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.databinding.UpcomingItemBinding
import com.celestial.movieapp.ui.MainFragment

val TAG = UpcomingAdapter::class.simpleName

class UpcomingAdapter(context: Context, val fav: MainFragment.FavInterface) :
    PagingDataAdapter<MovieModel, UpcomingAdapter.MovieItemViewHolder>(DIFF_CALLBACK) {

    var glide: RequestManager = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding =
            UpcomingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        with(holder) {
            getItem(position)?.let {
                binding.tvPopular.text = it.title
                glide.load("https://image.tmdb.org/t/p/w500" + it.backdropPath)
                    .into(binding.imgvPopular)
                if (it.isFav)
                    binding.imgvFavBtn.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_fav_solid))
                else
                    binding.imgvFavBtn.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_fav_outline))

                binding.imgvFavBtn.setOnClickListener { v ->
                    it.isFav = true
                    fav.setFav(it)
                    Log.d(TAG,"Favourite Click"+it.toString())
                }
            }
        }
    }


    inner class MovieItemViewHolder(val binding: UpcomingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}