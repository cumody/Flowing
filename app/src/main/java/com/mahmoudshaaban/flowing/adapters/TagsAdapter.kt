package com.mahmoudshaaban.flowing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mahmoudshaaban.flowing.R
import com.mahmoudshaaban.flowing.databinding.TagItemLayoutBinding
import com.mahmoudshaaban.flowing.util.loadImage
import com.wajahatkarim3.imagine.model.TagModel

class TagsAdapter(val onTagSelected: (tag: TagModel, position: Int) -> Unit) :
    RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {

    private val tagItems: ArrayList<TagModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.TagViewHolder {
        var binding = TagItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagsAdapter.TagViewHolder, position: Int) {
        holder.bind(tagItems[position], position)
    }

    override fun getItemCount(): Int = tagItems.size


    fun updateItems(tagsList: List<TagModel>) {
        tagItems.clear()
        tagItems.addAll(tagsList)
        notifyDataSetChanged()
    }


    inner class TagViewHolder(val itemBinding: TagItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(tagModel: TagModel, position: Int) {
            itemBinding.apply {
                txtTagName.text = tagModel.tagName

                imgTag.load(tagModel.imageUrl) {
                    placeholder(R.color.color_box_background)
                    crossfade(true)
                }

                cardTag.setOnClickListener {
                    onTagSelected(tagModel, position)
                }
            }
        }
    }
}