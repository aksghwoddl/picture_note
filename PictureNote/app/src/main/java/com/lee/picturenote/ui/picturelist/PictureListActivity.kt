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
    }

    override fun onStart() {
        super.onStart()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPictureList(1)
    }
    
    private fun observeData() {
        with(viewModel) {
            model.observe(this@PictureListActivity) {
                Log.d(TAG, "observeData: ${model.value}")
            }

            toastMessage.observe(this@PictureListActivity) {
                Toast.makeText(this@PictureListActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}