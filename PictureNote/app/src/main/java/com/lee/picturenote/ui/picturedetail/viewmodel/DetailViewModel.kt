package com.lee.picturenote.ui.picturedetail.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.picturenote.domain.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 상세 페이지 ViewModel class
 * **/
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

}