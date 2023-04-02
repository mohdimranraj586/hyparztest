package com.hyparz.ui.notification

import com.hyparz.data.model.response.NotificationListResponse
import com.hyparz.utils.CommonNavigator

interface NotificationNavigator : CommonNavigator {

    /**
     * This all method is used to provide callback when clicking on fields.
     */
    fun showBottomProgress()
    fun hideBottomProgress()
    fun showPageLoader()
    fun showHideLoader()
    fun getNotificationListResponse(notificationListResponse: NotificationListResponse)
    fun tryAgain()
    fun backToPreviousActivity()
}