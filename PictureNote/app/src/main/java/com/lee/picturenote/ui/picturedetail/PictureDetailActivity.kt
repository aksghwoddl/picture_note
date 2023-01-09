package com.lee.picturenote.ui.picturedetail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import com.lee.picturenote.R
import com.lee.picturenote.common.EXTRA_SELECTED_PICTURE
import com.lee.picturenote.common.FIRST_ITEM_ID
import com.lee.picturenote.common.LAST_ITEM_ID
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.databinding.ActivityPictureDetailBinding
import com.lee.picturenote.ui.picturedetail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

private const val CLICK_TIME_OUT = 1000L
private const val TAG = "PictureDetailActivity"
/**
 * 상세페이지 activity class
 * **/
@AndroidEntryPoint
class PictureDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPictureDetailBinding
    private lateinit var compositeDisposable: CompositeDisposable
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
        addClickListener()
        viewModel.setPicture(selectedPicture)
    }

    override fun onDestroy() {
        compositeDisposable.clear() // RxBinding 사용 후 생성된 Disposable객체 clear
        super.onDestroy()
    }

    private fun observeData() {
        with(viewModel){
            selectedPicture.observe(this@PictureDetailActivity){
                settingContents(it)
            }
            toastMessage.observe(this@PictureDetailActivity){
                Toast.makeText(this@PictureDetailActivity , it , Toast.LENGTH_SHORT).show()
            }
            isProgress.observe(this@PictureDetailActivity){
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            previousButtonEnable.observe(this@PictureDetailActivity){
                binding.previousButton.isEnabled = it
            }
            nextButtonEnable.observe(this@PictureDetailActivity){
                binding.nextButton.isEnabled = it
            }
        }
    }

    /**
     * Contents들에 대해 setting해주는 함수
     * **/
    private fun settingContents(picture : Picture) {
        with(binding){
            Glide.with(this@PictureDetailActivity)
                .load(picture.downloadUrl)
                .error(R.drawable.no_image)
                .into(pictureImageView)

            authorTextView.text = String.format(getString(R.string.detail_author) , picture.author)
            widthTextView.text = String.format(getString(R.string.detail_width) , picture.width)
            heightTextView.text = String.format(getString(R.string.detail_height) , picture.height)
            Log.d(TAG, "settingContents: ${picture.id.toInt()}")
            when(picture.id.toInt()){
                FIRST_ITEM_ID -> viewModel.setPreviousButtonEnable(false)
                LAST_ITEM_ID -> viewModel.setNextButtonEnable(false)
                else -> {
                    viewModel.setPreviousButtonEnable(true)
                    viewModel.setNextButtonEnable(true)
                }
            }
        }
        viewModel.setProgress(false)
    }

    /**
     * Next , Previous button 클릭 리스너 등록하는 함수
     * **/
    private fun addClickListener() {
        compositeDisposable = CompositeDisposable()
        with(binding){
            val prevDisposable = previousButton.clicks()
                .throttleFirst(CLICK_TIME_OUT , TimeUnit.MILLISECONDS)
                .subscribe{
                    viewModel.selectedPicture.value?.let {
                        val id = it.id.toInt() - 1
                        viewModel.getPictureByButtonClick(id)
                    }

                }
            val nextDisposable = nextButton.clicks()
                .throttleFirst(CLICK_TIME_OUT , TimeUnit.MILLISECONDS)
                .subscribe {
                    viewModel.selectedPicture.value?.let {
                        val id = it.id.toInt() + 1
                        viewModel.getPictureByButtonClick(id)
                    }
                }
            compositeDisposable.addAll(prevDisposable , nextDisposable)
        }
    }

    fun onBackButton() {
        finish()
    }
}