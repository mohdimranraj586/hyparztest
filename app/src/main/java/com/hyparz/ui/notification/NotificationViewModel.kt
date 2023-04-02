package com.hyparz.ui.notification

import com.hyparz.R
import com.hyparz.data.local.AppPreference
import com.hyparz.data.model.response.NotificationListResponse
import com.hyparz.ui.base.BaseViewModel
import com.hyparz.utils.NetworkResponseCallback
import java.util.*

class NotificationViewModel : BaseViewModel<NotificationNavigator>() {

    private var requestParam: HashMap<String, Any>? = null

    fun initView() {
        navigator!!.init()
    }

    /**
     * This method is used to apply click event on fields.
     */
    fun tryAgain() {
        navigator!!.tryAgain()
    }

    fun backToPreviousActivity() {
        navigator!!.backToPreviousActivity()
    }

    /**
     * This method is used to call notification API.
     */
    internal fun notificationAPI(
        showProgress: Boolean,
        appPreferences: AppPreference,
        offset: String
    ) {
        if (showProgress) {
            navigator!!.showPageLoader()
        } else {
            navigator!!.showProgressBar()
        }
        disposable.add(
            NotificationListResponse().doNetworkRequest(prepareRequestHashMap(offset),
                appPreferences,
                object : NetworkResponseCallback<NotificationListResponse> {
                    override fun onResponse(notificationListResponse: NotificationListResponse) {
                        navigator!!.hideProgressBar()
                        navigator!!.showHideLoader()
                        if (notificationListResponse.isSuccess) {
                            navigator!!.getNotificationListResponse(notificationListResponse)
                        } else {
                            if (!notificationListResponse.message.equals("")) {
                                onServerError(notificationListResponse.message)
                            } else {
                                onServerError(notificationListResponse.errorBean!!)
                            }
                        }
                    }

                    override fun onFailure(message: String) {
                        navigator!!.hideProgressBar()
                        navigator!!.showHideLoader()
                        navigator!!.showValidationError("ERROR")
                    }

                    override fun onServerError(error: String) {
                        navigator!!.hideProgressBar()
                        navigator!!.showHideLoader()
                        if (error != null && error != "")
                            navigator!!.showValidationError(error)
                        else {
                            navigator!!.showValidationError("ERROR")
                        }
                    }

                    override fun onSessionExpire(error: String) {
                        navigator!!.hideProgressBar()
                        navigator!!.showHideLoader()
                        navigator!!.showSessionExpireAlert()
                    }

                    override fun onUpdateAppVersion(message: String) {
                        navigator!!.hideProgressBar()
                        navigator!!.showHideLoader()
                        navigator!!.onUpdateAppVersion(message)
                    }
                })
        )
    }

    /**
     * This method is used to send parameter on server.
     */
    private fun prepareRequestHashMap(offset: String): HashMap<String, Any> {
        requestParam = HashMap()

//        requestParam!!["result"] = limit
        requestParam!!["results"] = offset
       // requestParam!!["limit"] = limit
        return requestParam!!
    }
}