package com.lee.picturenote.ui.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakewharton.rxbinding3.view.clicks
import com.lee.picturenote.R
import com.lee.picturenote.databinding.ActivitySearchBinding
import com.lee.picturenote.ui.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

private const val TAG = "SearchActivity"
private const val DOUBLE_CLICK_TIMEOUT = 1000L

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    private val viewModel : SearchViewModel by viewModels()
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_search)
        binding.searchActivity = this@SearchActivity
        binding.searchViewModel = viewModel
        addListeners()
        observeData()
    }

    override fun onDestroy() {
        compositeDisposable.clear() // Memory Leak을 막기 위한 처리
        super.onDestroy()
    }

    private fun addListeners() {
        compositeDisposable = CompositeDisposable()
        with(binding){
            val submitDisposable =  submitButton.clicks()// 검색하기 버튼
                .throttleFirst(DOUBLE_CLICK_TIMEOUT , TimeUnit.MILLISECONDS) // 더블 클릭 방지
                .subscribe {
                if(checkEmpty()){
                    widthEditText.clearFocus()
                    heightEditText.clearFocus()
                    viewModel.setIsProgress(true)
                    hideSoftInput()
                    loadImage()
                }
            }

            grayScaleSwitch.setOnCheckedChangeListener {  _ , isChecked -> // GrayScale 스위치
                viewModel.setGrayScale(isChecked)
            }

            blurSwitch.setOnCheckedChangeListener { _ , isChecked -> // Blur 스위치
                viewModel.setBlur(isChecked)
                if(isChecked){ // Blur 게이지는 Blur 스위치가 켜져 있을때만 나타난다.
                    blurGageLayout.visibility = View.VISIBLE
                } else {
                    blurGageLayout.visibility = View.GONE
                }
            }
            compositeDisposable.add(submitDisposable)
        }
    }

    private fun observeData() {
        with(viewModel){
            grayScale.observe(this@SearchActivity){ // GrayScale
                Log.d(TAG, "observeData: grayScale = $it")
                if(it){
                    binding.blurSwitch.isChecked = false // Blur와 동시에 적용 불가능하기에 해당 버튼이 켜지면 Blur는 Off
                }
            }

            blur.observe(this@SearchActivity){ // Blur
                Log.d(TAG, "observeData: blur = $it")
                if(it){
                    binding.grayScaleSwitch.isChecked = false // GrayScale과 동시에 적용 불가능하기에 해당 버튼이 켜지면 GrayScale은 Off
                }
            }

            blurGage.observe(this@SearchActivity){ // Blur 게이지
                binding.blurGageTextView.text = String.format(resources.getString(R.string.blur_gage) , it)
            }

            toastMessage.observe(this@SearchActivity){ // Toast Message
                Toast.makeText(this@SearchActivity , it , Toast.LENGTH_SHORT).show()
            }

            isProgress.observe(this@SearchActivity){ // 진행 상황
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Glide를 통한 이미지 로딩하는 함수
     * **/
    private fun loadImage() {
        val url = viewModel.getSearchingImage()
        Glide.with(this@SearchActivity)
            .load(url)
            .listener(GlideRequestListener())
            .skipMemoryCache(true) // Memory 캐싱 하지 않기 (같은 URL에서 요청시 다른 이미지를 받아오기 위해)
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Disk 캐싱 하지 않기 (같은 URL에서 요청시 다른 이미지를 받아오기 위해)
            .into(binding.resultImageView)
    }

    /**
     * EditText가 비어 있는지 체크하는 함수
     * **/
    private fun checkEmpty() : Boolean {
        with(binding){
            widthEditText.run { // 넓이가 비어있을때
                if(text.isEmpty()){
                    viewModel.setToastMessage(resources.getString(R.string.input_width))
                    requestFocus()
                    return false
                }
            }
            heightEditText.run { // 높이가 비어있을때
                if(text.isEmpty()){
                    viewModel.setToastMessage(resources.getString(R.string.input_height))
                    requestFocus()
                    return false
                }
            }
        }
        return true
    }

    /**
     * Keyboard 숨기는 함수
     * **/
    private fun hideSoftInput() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken , 0)
    }

    /**
     * Glide의 error 처리를 위한 listener class
     * **/
    private inner class GlideRequestListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            exception : GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.d(TAG, "onLoadFailed: exception = $exception")
            viewModel.setToastMessage(resources.getString(R.string.not_found_image))
            viewModel.setIsProgress(false)
            hideSoftInput()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            viewModel.setIsProgress(false)
            return false
        }
    }
}