package com.celestial.movieapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.celestial.movieapp.R
import com.celestial.movieapp.data.model.MovieModel
import com.celestial.movieapp.databinding.MovieItemBinding

import com.celestial.movieapp.ui.MainFragment


class MovieAdapter(
    context: Context, val favInterface: MainFragment.FavInterface,
    val itemClickCallback: MainFragment.ItemClickCallback
) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    val TAG = MovieAdapter::class.simpleName

    var movies: ArrayList<MovieModel> = ArrayList<MovieModel>()



    fun addMovies(movieList: List<MovieModel>) {
        val pos = movies.size
        movies.addAll(movieList)

        notifyItemInserted(pos)
    }

    fun clearMovies() {
        movies.clear()
        notifyDataSetChanged()
    }

    var glide: RequestManager = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )

    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d(TAG, "On Create View")
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Log.d(TAG, movies.size.toString())

        with(holder) {
            movies[position]?.let {
                binding.tvPopular.text = it.title
                glide.load("https://image.tmdb.org/t/p/w500" + it.backdropPath)
                    .into(binding.imgvPopular)

                binding.imgvFavBtn.setOnClickListener { v ->
                    val r = !it.isFav


                    it.isFav = r
                    favInterface.setFav(it)
                    setFav(binding.imgvFavBtn,itemView.context,r)
                    Log.d(TAG, "Favourite Click" + it.toString())
                }
                setFav(binding.imgvFavBtn,holder.itemView.context,it.isFav)

            }

            this.itemView.setOnClickListener {
                itemClickCallback.itemClick(movies[bindingAdapterPosition])
            }

        }

    }
    fun setFav(imgv: ImageView, context: Context, isFav: Boolean) {
        if (isFav)
            imgv.setImageDrawable(context.getDrawable(R.drawable.ic_fav_solid))
        else
            imgv.setImageDrawable(context.getDrawable(R.drawable.ic_fav_outline))
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}