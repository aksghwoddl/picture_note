package com.lee.picturenote.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * RecyclerView의 ViewHolder의 생산성을 위한 추상 class
 * **/
abstract class PictureViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(data : Any)
}