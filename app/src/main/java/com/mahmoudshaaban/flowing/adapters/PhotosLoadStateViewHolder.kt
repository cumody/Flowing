package com.mahmoudshaaban.flowing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudshaaban.flowing.R
import com.mahmoudshaaban.flowing.databinding.LoadStateViewItemBinding

class PhotosLoadStateViewHolder(
    private val binding: LoadStateViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PhotosLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_view_item, parent, false)
            val binding = LoadStateViewItemBinding.bind(view)
            return PhotosLoadStateViewHolder(binding, retry)
        }
    }
}