package com.lee.domain.usecase

import com.lee.domain.repository.MainRepository
import javax.inject.Inject

class GetFavoritePictureCountUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun invoke() : Int {
        return repository.getFavoritePictureCount()
    }
}