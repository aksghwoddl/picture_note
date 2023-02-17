package com.lee.domain.usecase

import com.lee.domain.model.remote.Picture
import com.lee.domain.repository.MainRepository
import javax.inject.Inject

class GetPictureListUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun invoke(page : Int) : MutableList<Picture>{
        return repository.getPictureList(page)
    }
}