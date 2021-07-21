package com.mahmoudshaaban.flowing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudshaaban.flowing.R
import com.mahmoudshaaban.flowing.databinding.PhotoItemLayoutBinding
import com.mahmoudshaaban.flowing.model.PhotoModel
import com.mahmoudshaaban.flowing.util.loadImage

class PhotosViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){

    private val image: ImageView = itemView.findViewById(R.id.imgPhoto)

    private var repo: PhotoModel? = null


    fun bind(photo: PhotoModel) = with(itemView) {
        image.apply {
            transitionName = photo.urls.small
            loadImage(photo.urls.small)
        }

    }
}