package com.lee.domain.usecase

import com.lee.domain.model.remote.Picture
import com.lee.domain.repository.MainRepository
import javax.inject.Inject

class GetPictureByIdUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun invoke(id : String) : Picture {
        return repository.getPictureById(id)
    }
}