package com.lee.picturenote.ui.favoritelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.picturenote.R
import com.lee.picturenote.data.local.entity.FavoritePicture
import com.lee.picturenote.databinding.PictureItemBinding
import com.lee.picturenote.interfaces.OnItemClickListener
import com.lee.picturenote.ui.viewholder.PictureViewHolder

/**
 * 즐갸찾기 목록을 관리하는 RecyclerAdapter class
 * **/
class FavoriteRecyclerAdapter : RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {
    private var favoritePictureList = mutableListOf<FavoritePicture>()
    private lateinit var itemClickListener : OnItemClickListener

    fun setFavoritePictureList(list : MutableList<FavoritePicture>){
        favoritePictureList = list
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = PictureItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoritePictureList[position])
    }

    override fun getItemCount() = favoritePictureList.size


    inner class FavoriteViewHolder(private val binding: PictureItemBinding) : PictureViewHolder(binding){
        override fun bind(data: Any) {
            if(data is FavoritePicture){
                with(binding){
                    val favoritePicture = data.picture

                    favoriteIcon.visibility = View.GONE // 즐겨찾기에 화면에서는 아이콘을 사용하지 않음
                    Glide.with(binding.root)
                        .load(favoritePicture.downloadUrl)
                        .error(R.drawable.no_image)
                        .into(pictureImageView)

                    authorTextView.run {

                        text = favoritePicture.author
                    }
                    val size = "${favoritePicture.width} x ${favoritePicture.height}"
                    sizeTextView.text = size
                }

                val position = adapterPosition
                if(adapterPosition != RecyclerView.NO_POSITION){
                    itemView.setOnClickListener {
                        itemClickListener.onClick(it , data , position)
                    }
                }
            }
        }
    }
}