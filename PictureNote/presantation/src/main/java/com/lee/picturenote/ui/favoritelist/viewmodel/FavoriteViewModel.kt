package com.lee.picturenote.ui.favoritelist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.domain.model.local.entity.FavoritePicture
import com.lee.domain.model.local.entity.PictureEntity
import com.lee.domain.usecase.DeleteFavoritePictureUseCase
import com.lee.domain.usecase.GetFavoritePictureUseCase
import com.lee.domain.usecase.UpdateFavoritePictureUseCase
import com.lee.picturenote.R
import com.lee.picturenote.common.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoritePictureUseCase: GetFavoritePictureUseCase ,
    private val deleteFavoritePictureUseCase: DeleteFavoritePictureUseCase ,
    private val updateFavoritePictureUseCase: UpdateFavoritePictureUseCase ,
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
                getFavoritePictureUseCase.invoke()
            }
            if(favoritePicture.isEmpty()){
                _isEmptyList.value = true
            } else {
                _isEmptyList.value = false
                _favoritePictures.value = favoritePicture
            }
        }
    }

    /**
     * 즐겨찾기를 삭제하는 함수
     * **/
    fun deleteFavoritePicture(favoritePicture : FavoritePicture){
        viewModelScope.launch {
            with(favoritePicture){
                val pictureEntity =
                    PictureEntity(id, picture, index)
                withContext(Dispatchers.IO){
                    deleteFavoritePictureUseCase.invoke(pictureEntity)
                    getFavoritePictures()
                }
            }
            _toastMessage.value = resourceProvider.getString(R.string.delete_favorite)
        }
    }

    /**
     * 즐겨찾기 목록을 업데이트 하는 함수
     * **/
    fun updateFavoritePicture() {
        favoritePictures.value?.let {
            it.forEachIndexed{ index , favoritePicture ->
                favoritePicture.index = index
                with(favoritePicture){
                    val pictureEntity = PictureEntity(id, picture, index)
                    CoroutineScope(Dispatchers.IO).launch {
                        // viewModelScope를 통한 Coroutine을 내릴시에 Activity가 내려가고 ViewModel이 정리되면서
                        // Coroutine이 task를 완려하지 못하고 종료 해버리는 현상이 발생하여
                        // 해당 부분은 CoroutineScope를 사용합니다.
                        updateFavoritePictureUseCase.invoke(pictureEntity)
                    }
                }
            }
        }
    }
}