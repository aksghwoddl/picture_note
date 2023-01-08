package com.lee.picturenote.ui.picturelist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.lee.picturenote.data.remote.model.Picture

/**
 * RecyclerAdapter의 update를 관리하기 위한 DiffUtil의 Callback class
 * **/
class DiffUtilCallback(
    private val oldList : MutableList<Picture> , private val newList : MutableList<Picture>)
    : DiffUtil.Callback(){

    override fun getOldListSize() = oldList.size


    override fun getNewListSize() = newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}