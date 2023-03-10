package com.lee.picturenote.ui.favoritelist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lee.domain.model.local.FavoritePicture
import com.lee.picturenote.R
import com.lee.picturenote.common.EXTRA_UPDATE_ID
import com.lee.picturenote.common.INTENT_RELEASE_FAVORITE
import com.lee.picturenote.common.wrapper.GridLayoutManagerWrapper
import com.lee.picturenote.databinding.ActivityFavoriteListBinding
import com.lee.picturenote.interfaces.OnItemClickListener
import com.lee.picturenote.ui.favoritelist.adapter.FavoriteRecyclerAdapter
import com.lee.picturenote.ui.favoritelist.adapter.ItemTouchHelperCallBack
import com.lee.picturenote.ui.favoritelist.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 즐겨찾기 Activity class
 * **/
@AndroidEntryPoint
class FavoriteListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteListBinding
    private val viewModel : FavoriteViewModel by viewModels()

    private lateinit var favoriteRecyclerAdapter: FavoriteRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@FavoriteListActivity , R.layout.activity_favorite_list)
        binding.favoriteActivity = this@FavoriteListActivity
        binding.favoriteViewModel = viewModel

        initRecyclerView()
        observeData()
        viewModel.getFavoritePictures() // 즐겨찾기한 목록 불러오기
    }

    private fun initRecyclerView() {
        favoriteRecyclerAdapter = FavoriteRecyclerAdapter()
        favoriteRecyclerAdapter.setItemClickListener(ItemClickListener())
        binding.favoriteRecyclerView.run {
            layoutManager = GridLayoutManagerWrapper(this@FavoriteListActivity , 2)
            adapter = favoriteRecyclerAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false // RecyclerView 깜빡임 현상 없애기
        }
        val itemTouchHelperCallBack = ItemTouchHelperCallBack(favoriteRecyclerAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.favoriteRecyclerView)
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateFavoritePicture() // 화면이 내려가면 update를 시작한다.
    }

    private fun observeData() {
        with(viewModel){
            favoritePictures.observe(this@FavoriteListActivity){ // 즐겨찾기한 data 목록
                favoriteRecyclerAdapter.run {
                    setFavoritePictureList(it)
                    notifyItemRangeChanged(0 , itemCount)
                }
            }

            toastMessage.observe(this@FavoriteListActivity){ // Toast Message
                Toast.makeText(this@FavoriteListActivity , it , Toast.LENGTH_SHORT).show()
            }

            isEmptyList.observe(this@FavoriteListActivity){ // List가 비어있는지에 대한 여부
                if(it){
                    binding.emptyLayout.visibility = View.VISIBLE
                    binding.favoriteRecyclerView.visibility = View.GONE
                } else {
                    binding.favoriteRecyclerView.visibility = View.VISIBLE
                    binding.emptyLayout.visibility = View.GONE
                }
            }
        }
    }

    /**
     * RecyclerView의 ItemClickListener
     * **/
    private inner class ItemClickListener : OnItemClickListener{
        override fun onClick(view: View, model: Any, position: Int) {
            if(model is FavoritePicture){
                val alertBuilder = AlertDialog.Builder(this@FavoriteListActivity)
                alertBuilder.setTitle(getString(R.string.favorite))
                    .setMessage(R.string.delete_dialog_message)
                    .setNegativeButton(R.string.cancel) {dialog , _  -> dialog.dismiss()}
                    .setPositiveButton(R.string.confirm) { dialog , _ ->
                        viewModel.deleteFavoritePicture(model)
                        with(Intent(INTENT_RELEASE_FAVORITE)){
                            putExtra(EXTRA_UPDATE_ID , model.id)
                            sendBroadcast(this)
                        }
                        dialog.dismiss()
                    }.create().show()
            }
        }
    }
}