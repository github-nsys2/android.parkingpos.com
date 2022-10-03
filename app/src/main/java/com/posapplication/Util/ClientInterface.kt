package com.posapplication.Util


import retrofit2.Response
import retrofit2.http.*

interface ClientInterface {

    @GET(Const.API_PROCESS_PAYMENT)
    suspend fun paymentApi(
        @Query("Token") Token: String?,
        @Query("Amount") amount: String?,
        @Query("DeviceID") deviceID: String?,
        ): Response<PaymentData>
}