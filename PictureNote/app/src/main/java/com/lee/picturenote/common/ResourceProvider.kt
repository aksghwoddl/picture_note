package com.lee.picturenote.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Resource를 사용하기 위한 Provider class
 * **/
@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context : Context
) {
    fun getString(id : Int) = context.getString(id)
}