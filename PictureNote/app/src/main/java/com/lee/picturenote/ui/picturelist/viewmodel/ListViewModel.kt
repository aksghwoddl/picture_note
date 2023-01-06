package com.lee.picturenote.ui.picturelist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.domain.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * 그림 목록 ViewModel class
 * **/
@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val model = MutableLiveData<MutableList<Picture>>()
    val toastMessage = MutableLiveData<String>()

    fun getPictureList(page : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getPictureList(page)
            if(response.isSuccessful){
                CoroutineScope(Dispatchers.Main).launch {
                    model.value = response.body()
                }
            } else {
                onError("문제발생!")
            }
        }
    }

    private fun onError(message : String) {
        toastMessage.postValue(message)
    }
}