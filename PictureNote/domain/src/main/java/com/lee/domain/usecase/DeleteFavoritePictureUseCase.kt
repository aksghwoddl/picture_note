package com.lee.domain.usecase

import com.lee.domain.model.local.entity.PictureEntity
import com.lee.domain.repository.MainRepository
import javax.inject.Inject

class DeleteFavoritePictureUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun invoke(pictureEntity: PictureEntity){
        repository.deleteFavoritePicture(pictureEntity)
    }
}