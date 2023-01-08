package com.lee.picturenote.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 본 앱의 MainApplication
 * **/
@HiltAndroidApp
class PictureNoteApplication : Application() {
    companion object{
        private lateinit var instance : PictureNoteApplication
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = PictureNoteApplication()
    }
}