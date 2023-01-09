package com.lee.picturenote.interfaces

import android.view.View
import com.lee.picturenote.data.remote.model.Picture

/**
 * ItemClick Interface
 * **/
interface OnItemClickListener {
    fun onClick(view : View, model : Picture, position : Int)
}