package com.lee.picturenote.interfaces

import android.view.View

/**
 * ItemClick Interface
 * **/
interface OnItemClickListener {
    fun onClick(view : View, model : Any , position : Int)
}