package com.lee.picturenote.ui.picturelist

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lee.picturenote.databinding.ActivityPictureListBinding
import com.lee.picturenote.ui.picturelist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "PictureListActivity"

/**
 * 그림 목록 Activity class
 * **/
@AndroidEntryPoint
class PictureListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPictureListBinding
    private val viewModel : ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureListBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        observeData()
    }
    
    private fun observeData() {
        with(viewModel) {
            pictures.observe(this@PictureListActivity){ // 사진 목록
                Log.d(TAG, "observeData: $it")
                // RecyclerView에 data 처리 부분 구현 필요
            }
            
            page.observe(this@PictureListActivity){ // Page
                viewModel.getPictureList()
            }

            toastMessage.observe(this@PictureListActivity) { // Toast message
                Toast.makeText(this@PictureListActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}