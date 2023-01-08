package com.lee.picturenote.ui.picturedetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.interfaces.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun setPicture(picture: Picture){
        _selectedPicture.value = picture
    }
}