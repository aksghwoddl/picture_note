package com.lee.picturenote.ui.picturedetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.picturenote.R
import com.lee.picturenote.common.PictureNoteApplication
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.interfaces.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * 상세 페이지 ViewModel class
 * **/
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _selectedPicture = MutableLiveData<Picture>()
    val selectedPicture : LiveData<Picture>
    get() = _selectedPicture

    private val _nextButtonEnable = MutableLiveData<Boolean>()
    val nextButtonEnable : LiveData<Boolean>
    get() = _nextButtonEnable

    private val _previousButtonEnable = MutableLiveData<Boolean>()
    val previousButtonEnable : LiveData<Boolean>
        get() = _previousButtonEnable

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
    get() = _toastMessage

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
    get() = _isProgress

    fun setPicture(picture: Picture){
        _selectedPicture.value = picture
    }

    fun setProgress(progress : Boolean){
        _isProgress.value = progress
    }

    fun setPreviousButtonEnable(enable : Boolean){
        _previousButtonEnable.value = enable
    }
    fun setNextButtonEnable(enable : Boolean){
        _nextButtonEnable.value = enable
    }

    /**
     * Activity에서 Button click시에 호출되는 함수 (Id에 따라 그림 정보를 불러온다.)
     * **/
    fun getPictureByButtonClick(id : Int) {
        viewModelScope.launch {
            _isProgress.value = true
            try{
                val response = withContext(Dispatchers.IO){
                    repository.getPictureById(id.toString())
                }
                if(response.isSuccessful){
                    _selectedPicture.value = response.body()
                } else {
                    onError(PictureNoteApplication.getInstance().getString(R.string.response_fail))
                }
            } catch (exception : SocketTimeoutException){
                onError(PictureNoteApplication.getInstance().getString(R.string.socket_time_out))
            }
        }
    }

    private fun onError(message : String){
        _toastMessage.postValue(message)
    }
}