package com.posapplication.ui.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendemailModelClass
{

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: String? = null
}