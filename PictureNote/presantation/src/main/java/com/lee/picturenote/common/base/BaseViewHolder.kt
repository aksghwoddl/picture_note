package com.lee.picturenote.common.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * RecyclerView의 ViewHolder의 생산성을 위한 추상 class
 * **/
abstract class BaseViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    /**
     * View를 data와 Bind하는 함수
     * **/
    abstract fun bind(data : Any)
}