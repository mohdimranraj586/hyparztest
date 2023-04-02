package com.hyparz.data.model.response

import com.hyparz.data.Parser
import com.hyparz.data.local.AppPreference
import com.hyparz.data.model.bean.NotificationBean
import com.hyparz.data.remote.ApiFactory
import com.hyparz.data.remote.ApiInterface
import com.hyparz.utils.AppConstants
import com.hyparz.utils.NetworkResponseCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class NotificationListResponse : BaseResponse<NotificationListResponse, String, Any>() {

    var data: NotificationBean? = null

    override fun doNetworkRequest(
        requestParam: HashMap<String, Any>,
        appPreference: AppPreference,
        networkResponseCallback: NetworkResponseCallback<NotificationListResponse>
    ): Disposable {
        val api = ApiFactory.clientWithHeader.create(ApiInterface::class.java)
        return api.notificationListAPI(requestParam
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { networkResponseCallback.onResponse(it) },
                { throwable -> Parser.parseErrorResponse(throwable, networkResponseCallback) })
    }
}