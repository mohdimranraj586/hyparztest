package com.hyparz.data.model.response

import com.google.gson.annotations.SerializedName
import com.hyparz.data.local.AppPreference
import com.hyparz.utils.NetworkResponseCallback
import io.reactivex.disposables.Disposable
import java.util.*

abstract class BaseResponse<T, K, V> {

    @SerializedName("success")
    var isSuccess: Boolean = false

    @SerializedName("message")
    var message: String = ""

    @SerializedName("status")
    var status: String = ""

    @SerializedName("error")
    var errorBean: String? = null

    abstract fun doNetworkRequest(
        requestParam: HashMap<K, V>,
        appPreference: AppPreference,
        networkResponseCallback: NetworkResponseCallback<T>
    ): Disposable
}
