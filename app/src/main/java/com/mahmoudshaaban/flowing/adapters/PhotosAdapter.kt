package com.mahmoudshaaban.flowing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudshaaban.flowing.R
import com.mahmoudshaaban.flowing.databinding.PhotoItemLayoutBinding
import com.mahmoudshaaban.flowing.model.PhotoModel

class PhotosAdapter : PagingDataAdapter<PhotoModel , RecyclerView.ViewHolder>(WALLPAPER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        PhotosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_item_layout, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as PhotosViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val WALLPAPER_COMPARATOR = object : DiffUtil.ItemCallback<PhotoModel>() {
            override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
                oldItem == newItem
        }
    }



}