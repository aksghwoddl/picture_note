package com.lee.picturenote.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 본 앱의 MainApplication
 * **/
@HiltAndroidApp
class MainApplication : Application() {
    companion object{
        private lateinit var instance : MainApplication
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = MainApplication()
    }
}