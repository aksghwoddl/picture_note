package com.lee.picturenote.ui.picturelist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.picturenote.R
import com.lee.picturenote.common.ResourceProvider
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.interfaces.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "ListViewModel"
/**
 * 그림 목록 ViewModel class
 * **/
@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: MainRepository ,
    private val resourceProvider: ResourceProvider
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
            try{
                val response = withContext(Dispatchers.IO){
                    val bResponse = repository.getPictureList(page.value!!)
                    checkFavorite(bResponse)
                }
                if(response.isSuccessful){
                    _pictures.value = response.body()
                } else {
                    Log.d(TAG, "getPictureList: response fail error code is ${response.code()}")
                    onError(resourceProvider.getString(R.string.response_fail))
                    _isProgress.value = false
                }
            } catch (exception : SocketTimeoutException){
                onError(resourceProvider.getString(R.string.socket_time_out))
                _isProgress.value = false
            }
        }
    }

    /**
     * 불러온 사진들중 즐겨찾기한 사진이 있는지 확인하는 함수
     * **/
    private suspend fun checkFavorite(response : Response<MutableList<Picture>>) : Response<MutableList<Picture>>{
        response.body()?.let { responsePictures ->
            val favoritePictures = repository.getFavoritePicture()
            favoritePictures.forEach { favoritePicture ->
                responsePictures.forEach { responsePicture ->
                    if(favoritePicture.id == responsePicture.id){
                        responsePicture.isFavorite = true
                    }
                }
            }
        }
        return response
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