package com.lee.domain.usecase

import com.lee.domain.model.local.entity.FavoritePicture
import com.lee.domain.repository.MainRepository
import javax.inject.Inject

class GetFavoritePictureByIdUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun invoke(id : String) : FavoritePicture? {
        return repository.getFavoritePictureById(id)
    }
}