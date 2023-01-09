package com.lee.picturenote.data.local.typeconverter

import com.google.gson.Gson
import com.lee.picturenote.data.remote.model.Picture

/**
 * 그림을 Room에 저장하기 위한 TypeConverter class
 * **/
class TypeConverter {
    @androidx.room.TypeConverter
    fun pictureToJson(picture: Picture) : String = Gson().toJson(picture)

    @androidx.room.TypeConverter
    fun jsonToPicture(json : String) : Picture = Gson().fromJson(json , Picture::class.java)
}