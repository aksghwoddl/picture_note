package com.lee.picturenote.ui.picturedetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lee.picturenote.databinding.ActivityPictureDetailBinding
import com.lee.picturenote.ui.picturedetail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * 상세페이지 activity class
 * **/
@AndroidEntryPoint
class PictureDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPictureDetailBinding
    private val viewModel : DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureDetailBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
}