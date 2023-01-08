package com.lee.picturenote.ui.picturelist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.databinding.ActivityPictureListBinding
import com.lee.picturenote.ui.picturelist.adapter.CustomLinearLayoutManager
import com.lee.picturenote.ui.picturelist.adapter.PictureRecyclerAdapter
import com.lee.picturenote.ui.picturelist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "PictureListActivity"
private const val RECYCLER_VIEW_BOTTOM = 1

/**
 * 그림 목록 Activity class
 * **/
@AndroidEntryPoint
class PictureListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPictureListBinding
    private val viewModel : ListViewModel by viewModels()

    private lateinit var pictureRecyclerAdapter: PictureRecyclerAdapter
    private lateinit var recyclerPictures : ArrayList<Picture>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureListBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        observeData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerPictures = arrayListOf()
        pictureRecyclerAdapter = PictureRecyclerAdapter()
        binding.imageRecyclerView.run {
            layoutManager = CustomLinearLayoutManager(context)
            adapter = pictureRecyclerAdapter
            addOnScrollListener(ScrollListener())
        }
    }
    
    private fun observeData() {
        with(viewModel) {
            pictures.observe(this@PictureListActivity){ // 사진 목록
                Log.d(TAG, "observeData: $it")
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
                viewModel.setProgress(true)
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
}