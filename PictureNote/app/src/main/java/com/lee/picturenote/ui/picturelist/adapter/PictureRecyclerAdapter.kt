package com.lee.picturenote.ui.picturelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.picturenote.R
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.databinding.PictureItemBinding
import com.lee.picturenote.ui.viewholder.PictureViewHolder

class PictureRecyclerAdapter : RecyclerView.Adapter<PictureRecyclerAdapter.PictureListViewHolder>() {
    private val pictures = ArrayList<Picture>()

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
    fun updateList(list : ArrayList<Picture>){
        val diffUtilCallback = DiffUtilCallback(pictures , list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        pictures.run {
            clear()
            addAll(list)
            diffResult.dispatchUpdatesTo(this@PictureRecyclerAdapter)
        }
    }

    inner class PictureListViewHolder(private val binding: PictureItemBinding) : PictureViewHolder(binding) {
        override fun bind(data: Any) {
            if(data is Picture){
                with(binding){
                    val url = data.downloadUrl
                    Glide.with(binding.root)
                        .load(url)
                        .error(R.drawable.no_image)
                        .into(pictureImageView)

                    authorTextView.text = data.author

                    val size = "${data.width} x ${data.height}"
                    sizeTextView.text = size
                }
            }
        }

    }
}