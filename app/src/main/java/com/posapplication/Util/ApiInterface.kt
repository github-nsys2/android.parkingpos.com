package com.posapplication.Util

import com.google.gson.JsonObject
import com.posapplication.ui.ModelClass.SendemailModelClass
import com.posapplication.ui.ModelClass.POSIDModelClass
import com.posapplication.ui.ModelClass.VehicleModelClass
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("NotifyUser/SendMailToUser")
    suspend fun sendemail(@Body recepientEmail: JsonObject?): Response<SendemailModelClass>?




//    @POST("ProjectApi/JsonViewer?")
//    fun getvalidposid(@Query("DeviceID") DeviceID: String,
//                     @Query("appVersion") appVersion: String,
//                     @Query("sqlcmd") sqlcmd: String,
//                     @Query("crudobject") crudobject: String,
//                     @Query("clientname") clientName: String)
//
//            : Call<POSIDModelClass?>?

  @GET("FsPos/Validate")
  suspend  fun getvalidposid(@Query("DeviceID") DeviceID: String,
                     @Query("Lat") Lat: String,
                     @Query("Lng") Lng: String)
  : Response<POSIDModelClass?>?

  @GET("FsPos/Validate")
    fun getvalidposidcall(@Query("DeviceID") DeviceID: String,
                     @Query("Lat") Lat: String,
                     @Query("Lng") Lng: String)
  : Call<POSIDModelClass?>?

  @POST("FsPos/SaveCard")
  suspend  fun setcardvalue(
      @Query("DeviceID") DeviceID: String,
      @Query("CardNumber") CardNumber: String,
      @Query("CustomerEmail") CustomerEmail: String,
      @Query("CustomerFullName") CustomerFullName: String,
      @Query("TransAmount") TransAmount: String,
      @Query("Lat") Lat: String,
      @Query("Lng") Lng: String)
  : Response<POSIDModelClass?>?

  @POST("FsPos/SaveFstrans")
  suspend  fun setvehiclevalue(
      @Body vehiclevalues: JsonObject?)
  : Response<POSIDModelClass?>?

//  @POST("FsPos/SaveFstrans")
//  suspend  fun setvehiclevalue(
//      @Query("DeviceID") DeviceID: String,
//      @Query("CardNumber") CardNumber: String,
////      @Query("CustomerEmail") CustomerEmail: String,
////      @Query("CustomerFullName") CustomerFullName: String,
//      @Query("TransAmount") TransAmount: String,
//      @Query("Lat") Lat: String,
//      @Query("Lng") Lng: String,
//      @Query("PlateNumber") PlateNumber: String,
//      @Query("imageURL") imageurl: String)
//  : Response<POSIDModelClass?>?


  @GET("FsPos/GetVehicleList")
  suspend  fun getvehicleList(
      @Query("DeviceID") DeviceID: String,
      @Query("Lat") Lat: String,
      @Query("Lng") Lng: String,
      @Query("CardNumber") CardNumber: String)
  : Response<VehicleModelClass?>?

}