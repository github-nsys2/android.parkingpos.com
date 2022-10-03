package com.posapplication.ui.ModelClass

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class VehicleModelClass {

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("VehicleNumber")
    @Expose
    var vehicleNumber: List<String>? = null
}