package com.celestial.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.celestial.movieapp.databinding.ItemLoadingStateBinding

class MoviesLoadingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<MoviesLoadingAdapter.LoadStateViewHolder>() {


    class LoadStateViewHolder(private val binding: ItemLoadingStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.btnRetry.setOnClickListener {
                    retry.invoke()
                }
            }

        fun bindState(loadState: LoadState){
            if (loadState is LoadState.Error){
                binding.tvErrorMessage.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.tvErrorMessage.isVisible = loadState !is LoadState.Loading
            binding.btnRetry.isVisible = loadState !is LoadState.Loading
        }
    }


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
       val vBinding = ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadStateViewHolder(vBinding, retry)
    }
}