package com.lee.picturenote.ui.favoritelist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView의 ItemTouch관련 이벤트를 관리하는 ItemTouchHelper의 CallBack class
 * **/
class ItemTouchHelperCallBack(listener : ItemTouchHelperListener) : ItemTouchHelper.Callback() {
    private var itemTouchHelperListener : ItemTouchHelperListener = listener

    interface ItemTouchHelperListener {
        fun onItemMove(from : Int , to : Int) : Boolean
        fun onItemSwipe(position : Int)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = ItemTouchHelper.END or ItemTouchHelper.START
        return makeMovementFlags(dragFlags , swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return itemTouchHelperListener.onItemMove(viewHolder.adapterPosition , target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemTouchHelperListener.onItemSwipe(viewHolder.adapterPosition)
    }

}