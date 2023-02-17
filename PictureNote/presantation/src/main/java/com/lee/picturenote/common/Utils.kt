package com.lee.picturenote.common

import android.content.Context
import android.net.ConnectivityManager

/**
 * Intent 수신을 위한 Key const
 * **/
const val EXTRA_SELECTED_PICTURE = "selected_picture"

/**
 * Rest 통신으로 얻은 Item 관련 const
 * **/
const val LAST_ITEM_ID = 1084
const val FIRST_ITEM_ID = 0



/**
 * Broadcast 관련 const
 * **/
const val INTENT_SETTING_FAVORITE = "com.lee.picturenote.common.INTENT_SETTING_FAVORITE"
const val INTENT_RELEASE_FAVORITE = "com.lee.picturenote.common.INTENT_RELEASE_FAVORITE"
const val EXTRA_UPDATE_ID = "update_id"

class Utils{
    companion object{
        /**
         * Network 연결을 체크하는 공용 함수
         * **/
        fun checkNetworkConnection(context : Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetwork
            return networkInfo.toString() != "null"
        }
    }
}