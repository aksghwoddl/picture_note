package com.lee.picturenote.common

/**
 * Intent 수신을 위한 Key const
 * **/
const val EXTRA_SELECTED_PICTURE = "selected_picture"
const val EXTRA_SELECTED_POSITION = "selected_position"

/**
 * Rest 통신으로 얻은 Item 관련 const
 * **/
const val LAST_ITEM_ID = 1084
const val FIRST_ITEM_ID = 0

/**
 * Room 관련 const
 * **/
const val DB_NAME = "favorite_db"
const val TABLE_NAME = "favorite_tbl"

/**
 * Broadcast 관련 const
 * **/
const val INTENT_SETTING_FAVORITE = "com.lee.picturenote.common.INTENT_SETTING_FAVORITE"
const val INTENT_RELEASE_FAVORITE = "com.lee.picturenote.common.INTENT_RELEASE_FAVORITE"
const val EXTRA_UPDATE_ID = "update_id"