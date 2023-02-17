package com.lee.data.model.local

import com.google.gson.Gson
import com.lee.domain.model.remote.Picture

/**
 * 그림을 Room에 저장하기 위한 TypeConverter class
 * **/
class TypeConverter {
    @androidx.room.TypeConverter
    fun pictureToJson(picture: Picture) : String = Gson().toJson(picture)

    @androidx.room.TypeConverter
    fun jsonToPicture(json : String) : Picture = Gson().fromJson(json , Picture::class.java)
}