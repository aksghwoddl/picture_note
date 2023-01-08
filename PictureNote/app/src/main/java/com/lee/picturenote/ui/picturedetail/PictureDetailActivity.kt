package com.lee.picturenote.ui.picturedetail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.lee.picturenote.EXTRA_SELECTED_PICTURE
import com.lee.picturenote.R
import com.lee.picturenote.data.remote.model.Picture
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
        binding = DataBindingUtil.setContentView(this , R.layout.activity_picture_detail)
        binding.detailViewModel = viewModel
        binding.detailActivity = this@PictureDetailActivity

        val selectedPicture : Picture = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){ // API Level 33 미만
            intent.extras?.getSerializable(EXTRA_SELECTED_PICTURE) as Picture
        } else { // API Level 33 이상
            intent.extras?.getSerializable(EXTRA_SELECTED_PICTURE , Picture::class.java) as Picture
        }
        observeData()
        viewModel.setPicture(selectedPicture)
    }

    private fun observeData() {
        with(viewModel){
            selectedPicture.observe(this@PictureDetailActivity){
                with(binding){
                    Glide.with(this@PictureDetailActivity)
                        .load(it.downloadUrl)
                        .error(R.drawable.no_image)
                        .into(pictureImageView)

                    authorTextView.text = String.format(getString(R.string.detail_author) , it.author)
                    widthTextView.text = String.format(getString(R.string.detail_width) , it.width)
                    heightTextView.text = String.format(getString(R.string.detail_height) , it.height)
                }
            }
        }
    }

    fun onBackButton() {
        finish()
    }
}