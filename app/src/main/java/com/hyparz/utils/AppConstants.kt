package com.hyparz.utils

import android.Manifest


/**
 *
 * Date   : 20-Jun-19.
 * Description : This class is used to define all constant fields
 */

object AppConstants {


    val INTERNAL_SERVER_ERROR = "Internal Server Error"
    val http_some_other_error = "Request time out, please try again."


    var ANDROID_DEVICE_ID = ""

    val LOG_CAT = "hyparzUser"
    val MEDIA_PERMISSION = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    val READ_WRITE_PERMISSION =
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

    val ERROR = "error"

    val PN_NAME = "name"
    val USER_DATA = "user_data"

    val COME_FROM = "ComeFrom"
    val NOTIFICATION = "notification"

    val NEW_NOTIFICATION = "NEW_NOTIFICATION"
}

