package com.lee.picturenote.ui.picturelist.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.picturenote.R
import com.lee.picturenote.common.ResourceProvider
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.databinding.PictureItemBinding
import com.lee.picturenote.interfaces.OnItemClickListener
import com.lee.picturenote.ui.viewholder.PictureViewHolder
import javax.inject.Inject

class PictureRecyclerAdapter : RecyclerView.Adapter<PictureRecyclerAdapter.PictureListViewHolder>() {
    private val pictures = ArrayList<Picture>()
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureListViewHolder {
        val binding = PictureItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return PictureListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureListViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    override fun getItemCount() = pictures.size

    /**
     * DiffUtil을 활용하여 List update하는 함수
     * **/
    fun updateList(list : ArrayList<Picture>) : DiffResult{
        val diffUtilCallback = DiffUtilCallback(pictures , list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        pictures.run {
            clear()
            addAll(list)
        }
        return diffResult
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        itemClickListener = listener
    }

    inner class PictureListViewHolder(private val binding: PictureItemBinding) : PictureViewHolder(binding) {
        override fun bind(data: Any) {
            if(data is Picture){
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
}