package com.lee.domain.usecase

import com.lee.domain.repository.MainRepository
import javax.inject.Inject

class GetIndexByIdUseCase  @Inject constructor(
    private val repository: MainRepository
){
    suspend fun invoke(id : String) : Int {
        return repository.getIndexById(id)
    }
}