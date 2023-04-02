package com.hyparz.data.remote

import com.hyparz.data.model.response.*
import com.hyparz.utils.AppConstants
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("api")
    fun notificationListAPI(
       // @Header("Authorization") access_token: String,
        @QueryMap map: Map<String, @JvmSuppressWildcards Any>
    ): Observable<NotificationListResponse>


}
