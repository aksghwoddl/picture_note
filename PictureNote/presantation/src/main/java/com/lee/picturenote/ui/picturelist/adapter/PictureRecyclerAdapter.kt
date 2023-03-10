package com.lee.picturenote.ui.picturelist.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.domain.model.remote.Picture
import com.lee.picturenote.R
import com.lee.picturenote.databinding.PictureItemBinding
import com.lee.picturenote.interfaces.OnItemClickListener
import com.lee.picturenote.common.base.BaseViewHolder

class PictureRecyclerAdapter : ListAdapter<Picture, PictureRecyclerAdapter.BaseListViewHolder>(DiffUtilCallback()) {
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder {
        val binding = PictureItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return BaseListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        itemClickListener = listener
    }

    inner class BaseListViewHolder(private val binding: PictureItemBinding) : BaseViewHolder(binding) {
        override fun bind(data: Any) {
            if(data is com.lee.domain.model.remote.Picture){
                with(binding){
                    val url = data.downloadUrl
                    Glide.with(binding.root) // ImageView
                        .load(url)
                        .error(R.drawable.no_image)
                        .into(pictureImageView)

                    authorTextView.text = data.author // 작가명

                    val size = "${data.width} x ${data.height}"
                    sizeTextView.text = size // 사진 크기

                    val favoriteDrawable : Drawable? = if(data.isFavorite){ // 사진 즐겨찾기 아이콘
                        ResourcesCompat.getDrawable(binding.root.context.resources , R.drawable.setting_favorite_icon , null )
                    } else {
                        ResourcesCompat.getDrawable(binding.root.context.resources , R.drawable.favorite_icon , null )
                    }
                    favoriteIcon.setImageDrawable(favoriteDrawable)
                }
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    itemView.setOnClickListener {
                        if(::itemClickListener.isInitialized){
                            itemClickListener.onClick(it , data , position)
                        }
                    }
                }
            }
        }

    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<Picture>() {
        override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return oldItem == newItem
        }
    }
}