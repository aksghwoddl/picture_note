package com.lee.picturenote.ui.picturelist.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.picturenote.R
import com.lee.picturenote.common.PictureNoteApplication
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.domain.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import javax.inject.Inject


/**
 * 그림 목록 ViewModel class
 * **/
@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _pictures = MutableLiveData<MutableList<Picture>>()
    val pictures : LiveData<MutableList<Picture>>
    get() = _pictures

    private val _page = MutableLiveData(1)
    val page : LiveData<Int>
    get() = _page

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
    get() = _toastMessage

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
    get() = _isProgress

    /**
     * 사진 목록을 가져오는 함수
     * **/
    fun getPictureList() {
        viewModelScope.launch {
            _isProgress.value = true
            var currentPage = 1
            page.value?.let {
                currentPage = it
            }
            try{
                val response = withContext(Dispatchers.IO){
                    repository.getPictureList(currentPage)
                }
                if(response.isSuccessful){
                    _isProgress.value = false
                    _pictures.value = response.body()
                } else {
                    onError(PictureNoteApplication.getInstance().getString(R.string.response_fail))
                }
            } catch (exception : SocketTimeoutException){
                onError(PictureNoteApplication.getInstance().getString(R.string.socket_time_out))
            }
        }
    }

    /**
     * Page를 setting하는 함수
     * **/
    fun setPage(page : Int) {
        _page.value = page
    }

    /**
     * Progress를 setting하는 함수
     * **/
    fun setProgress(isProgress : Boolean){
        _isProgress.value = isProgress
    }

    /**
     * Error 관리 함수
     * **/
    private fun onError(message : String) {
        _isProgress.value = false
        _toastMessage.postValue(message)
    }
}