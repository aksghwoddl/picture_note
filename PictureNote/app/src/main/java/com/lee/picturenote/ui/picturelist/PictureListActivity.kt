package com.lee.picturenote.ui.picturelist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lee.picturenote.R
import com.lee.picturenote.common.EXTRA_SELECTED_PICTURE
import com.lee.picturenote.common.EXTRA_UPDATE_ID
import com.lee.picturenote.common.INTENT_RELEASE_FAVORITE
import com.lee.picturenote.common.INTENT_SETTING_FAVORITE
import com.lee.picturenote.common.adapter.CustomLinearLayoutManager
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.databinding.ActivityPictureListBinding
import com.lee.picturenote.interfaces.OnItemClickListener
import com.lee.picturenote.ui.favoritelist.FavoriteListActivity
import com.lee.picturenote.ui.picturedetail.PictureDetailActivity
import com.lee.picturenote.ui.picturelist.adapter.PictureRecyclerAdapter
import com.lee.picturenote.ui.picturelist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "PictureListActivity"
private const val RECYCLER_VIEW_BOTTOM = 1
private const val RECYCLER_VIEW_TOP = -1

/**
 * 그림 목록 Activity class
 * **/
@AndroidEntryPoint
class PictureListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPictureListBinding
    private val viewModel : ListViewModel by viewModels()

    private lateinit var pictureRecyclerAdapter: PictureRecyclerAdapter
    private lateinit var recyclerPictures : ArrayList<Picture>
    private lateinit var updateReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_picture_list)
        binding.listActivity = this@PictureListActivity
        observeData()
        initBroadcastReceiver()
        initRecyclerView()
        initTopButton()

    }

    override fun onDestroy() {
        unregisterReceiver(updateReceiver)
        super.onDestroy()
    }

    private fun initBroadcastReceiver() {
        updateReceiver = UpdateReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(INTENT_SETTING_FAVORITE)
        intentFilter.addAction(INTENT_RELEASE_FAVORITE)
        registerReceiver(updateReceiver , intentFilter)
    }

    private fun initRecyclerView() {
        recyclerPictures = arrayListOf()
        pictureRecyclerAdapter = PictureRecyclerAdapter()
        pictureRecyclerAdapter.setOnItemClickListener(ItemClickListener())
        binding.imageRecyclerView.run {
            layoutManager = CustomLinearLayoutManager(context)
            adapter = pictureRecyclerAdapter
            addOnScrollListener(ScrollListener())
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false // RecyclerView 깜빡임 현상 없애기
        }
    }

    private fun initTopButton() {
        binding.topFabButton.setOnClickListener{ // RecyclerView를 최상단으로 올리기
            // 많은 사진을 로드 했을때 smoothScrollToPosition 사용시에 최상단 까지 올라가는게 너무 오래걸림
            // 그렇기에 scrollToPosition으로 메소드 replace
            binding.imageRecyclerView.scrollToPosition(0)
            it.visibility = View.GONE // 최상단 도달했을때 버튼 사라지게 하기
        }
    }
    
    private fun observeData() {
        with(viewModel) {
            pictures.observe(this@PictureListActivity){ // 사진 목록
                updateList(it)
            }
            
            page.observe(this@PictureListActivity){ // Page
                viewModel.getPictureList()
            }

            toastMessage.observe(this@PictureListActivity) { // Toast message
                Toast.makeText(this@PictureListActivity, it, Toast.LENGTH_SHORT).show()
            }

            isProgress.observe(this@PictureListActivity){ // 상태 진행중 확인
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    /**
     * LiveData 변경시 그림 목록을 update하는 함수
     * **/
    private fun updateList(list : MutableList<Picture>) {
        if(::recyclerPictures.isInitialized){
            recyclerPictures.addAll(list)
            lifecycleScope.launch{
                val result = withContext(Dispatchers.Default){
                    pictureRecyclerAdapter.updateList(recyclerPictures)
                }
                result.dispatchUpdatesTo(pictureRecyclerAdapter)
                viewModel.setProgress(false)
            }
        }
    }

    /**
     * RecyclerView의 scroll listener
     * **/
    private inner class ScrollListener : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(RECYCLER_VIEW_TOP)){ // 최상단 도달시에는 button 숨김
                binding.topFabButton.visibility = View.GONE
            } else {
                binding.topFabButton.visibility = View.VISIBLE
            }

            if(!recyclerView.canScrollVertically(RECYCLER_VIEW_BOTTOM)){
                with(viewModel){
                    isProgress.value?.let {
                        if(!it){
                            viewModel.page.value?.let { currentPage ->
                                viewModel.setPage(currentPage + 1)
                            }
                        } else {
                            Log.d(TAG, "onScrollStateChanged: still loading image!")
                        }
                    }
                }
            }
        }
    }

    /**
     * RecyclerView의 ItemClick Listener
     * **/
    private inner class ItemClickListener : OnItemClickListener {
        override fun onClick(view: View, model: Any, position: Int) {
            if(model is Picture){
                with(Intent(this@PictureListActivity , PictureDetailActivity::class.java)){
                    putExtra(EXTRA_SELECTED_PICTURE , model)
                    startActivity(this)
                }
            }
        }
    }

    /**
     * 즐겨찾기 상태를 갱신하기 위해 사용하는 BroadcastReceiver
     * **/
    private inner class UpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context : Context?, intnet : Intent?) {
            when(intnet?.action){
                INTENT_RELEASE_FAVORITE -> {
                    Log.d(TAG, "onReceive: INTENT_RELEASE_FAVORITE")
                    val updateId = intnet.extras?.getString(EXTRA_UPDATE_ID)
                    updateFavorite(updateId!! , false)
                }
                INTENT_SETTING_FAVORITE -> {
                    Log.d(TAG, "onReceive: INTENT_SETTING_FAVORITE")
                    val updateId = intnet.extras?.getString(EXTRA_UPDATE_ID)
                    updateFavorite(updateId!! , true)
                }
            }
        }
    }

    /**
     * Broadcast를 통해 전달된 그림의 즐겨찾기 여부를 update하는 함수
     * **/
    private fun updateFavorite(id : String , isFavorite : Boolean) {
        recyclerPictures.forEachIndexed{ index , picture ->
            if(picture.id == id){
                picture.isFavorite = isFavorite
                pictureRecyclerAdapter.notifyItemChanged(index)
            }
        }
    }

    /**
     * 즐겨찾기 페이지 실행 하는 함수
     * **/
    fun startFavoriteActivity() {
        with(Intent(this@PictureListActivity , FavoriteListActivity::class.java)){
            startActivity(this)
        }
    }
}