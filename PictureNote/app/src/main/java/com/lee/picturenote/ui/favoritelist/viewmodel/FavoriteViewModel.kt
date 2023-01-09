package com.lee.picturenote.ui.favoritelist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.picturenote.common.ResourceProvider
import com.lee.picturenote.data.local.entity.FavoritePicture
import com.lee.picturenote.interfaces.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: MainRepository ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _favoritePictures = MutableLiveData<MutableList<FavoritePicture>>()
    val favoritePictures : LiveData<MutableList<FavoritePicture>>
    get() = _favoritePictures

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
    get() =  _toastMessage

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList : LiveData<Boolean>
    get() = _isEmptyList

    /**
     * 즐겨찾기한 그림 목록 불러오는 함수
     * **/
    fun getFavoritePictures() {
        viewModelScope.launch {
            val favoritePicture = withContext(Dispatchers.IO){
                repository.getFavoritePicture()
            }
            if(favoritePicture.isEmpty()){
                _isEmptyList.value = true
            } else {
                _isEmptyList.value = false
                _favoritePictures.value = favoritePicture
            }
        }
    }
}