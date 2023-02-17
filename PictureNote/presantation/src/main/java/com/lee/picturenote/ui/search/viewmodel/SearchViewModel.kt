package com.lee.picturenote.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.picturenote.common.BASE_URL
import com.lee.picturenote.interfaces.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val PATH_URL = "%s/"
private const val QUERY_GRAYSCALE = "?grayscale"
private const val QUERY_BLUR = "?blur=%d"
private const val QUERY_NO_BLUR_GAGE = "?blur"

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    val inputWidth = MutableLiveData<String>() // 입력된 넓이
    val inputHeight = MutableLiveData<String>() // 입력된 높이
    val blurGage = MutableLiveData<Int>(1) // Blur 게이지


    private val _grayScale = MutableLiveData<Boolean>(false)
    val grayScale : LiveData<Boolean>
    get() = _grayScale
    fun setGrayScale(on : Boolean){
        _grayScale.value = on
    }

    private val _blur = MutableLiveData<Boolean>(false)
    val blur : LiveData<Boolean>
    get() = _blur
    fun setBlur(on : Boolean){
        _blur.value = on
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
    get() = _toastMessage
    fun setToastMessage(message : String){
        _toastMessage.value = message
    }

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
    get() = _isProgress
    fun setIsProgress(isProgress : Boolean){
        _isProgress.value = isProgress
    }

    /**
     * LiveData를 통해 로딩할 이미지를 검색하는 함수
     * **/
    fun getSearchingImage() : String{
        var url = BASE_URL
        inputWidth.value?.let {
            if(it.isNotEmpty()){
                val path = String.format(PATH_URL , it)
                url += path
            }
        }
        inputHeight.value?.let {
            if(it.isNotEmpty()){
                val path = String.format(PATH_URL , it)
                url += path
            }
        }
        grayScale.value?.let {
            if(it){
                url += QUERY_GRAYSCALE
            }
        }
        blur.value?.let {
            if(it){
                url += if(blurGage.value == 0){
                    QUERY_NO_BLUR_GAGE
                } else {
                    String.format(QUERY_BLUR , blurGage.value)
                }
            }
        }
        Log.d(TAG, "getSearchingImage: url = $url")
        return url
    }
}