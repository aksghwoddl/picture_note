package com.lee.picturenote.ui.picturedetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.picturenote.R
import com.lee.picturenote.common.ERROR_CODE_404
import com.lee.picturenote.common.ResourceProvider
import com.lee.picturenote.data.local.entity.PictureEntity
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.interfaces.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "DetailViewModel"

/**
 * 상세 페이지 ViewModel class
 * **/
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository ,
    private val resourceProvider: ResourceProvider
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

    /**
     * 현재 보여주는 그림을 setting하는 함수
     * **/
    fun setPicture(picture: Picture){
        _selectedPicture.value = picture
    }

    /**
     * 진행상태를 setting하는 함수
     * **/
    fun setProgress(progress : Boolean){
        _isProgress.value = progress
    }

    /**
     * 이전 버튼 활성화 여부 설정하는 함수
     * **/
    fun setPreviousButtonEnable(enable : Boolean){
        _previousButtonEnable.value = enable
    }

    /**
     * 다음 버튼 활성화 여부 설정하는 함수
     * **/
    fun setNextButtonEnable(enable : Boolean){
        _nextButtonEnable.value = enable
    }

    /**
     * Activity에서 Button click시에 호출되는 함수 (Id에 따라 그림 정보를 불러온다.)
     * **/
    fun getPictureByButtonClick(id : Int , isNextClick : Boolean) {
        viewModelScope.launch {
            _isProgress.value = true
            try{
                val response = withContext(Dispatchers.IO){
                    val bResponse = repository.getPictureById(id.toString())
                    bResponse.body()?.let { picture ->
                        repository.getFavoritePictureById(picture.id)?.let {
                            picture.isFavorite = true
                        }
                    }
                    bResponse
                }
                if(response.isSuccessful){
                    _selectedPicture.value = response.body()
                } else {
                    Log.d(TAG, "getPictureByButtonClick: response is fail error code is ${response.code()}")
                    if(response.code() == ERROR_CODE_404){
                        // 버튼 클릭으로 사진 정보를 가져 올때 가끔 ID가 띄엄띄엄 있는 경우가 있어 404 response를 받게 됨
                        // 해당 경우에는 어떤 버튼이 클릭 됐는지 확인 후에 정보가 ID를 + / - 하며 이미지가 나올때까지 검색하도록 수정
                        val reSearchId : Int = if(isNextClick){ id + 1 } else { id -1 }
                        getPictureByButtonClick(reSearchId , isNextClick)
                    } else {
                        resourceProvider.getString(R.string.response_fail)
                        _isProgress.value = false
                    }
                }
            } catch (exception : SocketTimeoutException){
                onError(resourceProvider.getString(R.string.socket_time_out))
                _isProgress.value = false
            }
        }
    }

    /**
     * 즐겨찾기 추가하는 함수
     * **/
    fun addFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val index = repository.getFavoritePictureCount()
                with(selectedPicture.value!!){
                    this.isFavorite = true
                    val pictureEntity = PictureEntity(id , this , index)
                    repository.addFavoritePicture(pictureEntity)
                    _selectedPicture.postValue(this)
                }
            }
            _toastMessage.value = resourceProvider.getString(R.string.add_favorite)
        }
    }

    /**
     *  즐겨찾기 해제하는 함수
     * **/
    fun deleteFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                with(selectedPicture.value!!){
                    val index = repository.getIndexById(id)
                    this.isFavorite = false
                    val pictureEntity = PictureEntity(id , this , index)
                    repository.deleteFavoritePicture(pictureEntity)
                    _selectedPicture.postValue(this)
                }
            }
            _toastMessage.value = resourceProvider.getString(R.string.delete_favorite)
        }
    }

    /**
     * 현재 선택된 그림이 즐겨찾기된 그림인지 확인하는 함수
     * **/
    fun checkFavorite() {
        viewModelScope.launch {
            val favoritePicture = withContext(Dispatchers.IO){
                repository.getFavoritePictureById(selectedPicture.value!!.id)
            }
            favoritePicture?.let { // 현재 ID로 저장된 즐겨찾기가 있을 경우
                _selectedPicture.value = it.picture
            }?:let { // 현재 ID로 저장된 즐겨찾기가 없는 경우 Log찍도록 함
                Log.d(TAG, "checkFavorite: ${_selectedPicture.value!!.id} is not favorite picture!!")
            }
        }
    }

    fun onError(message : String){
        _toastMessage.postValue(message)
    }
}