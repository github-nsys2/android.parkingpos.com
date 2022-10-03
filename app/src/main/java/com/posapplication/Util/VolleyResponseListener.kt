package com.posapplication.Util

import com.android.volley.VolleyError
import org.json.JSONObject

interface VolleyResponseListener {

    fun onError(url: String?, message: VolleyError?)
    fun onResponse(url: String?, response: JSONObject?)
    fun onProgressStart()
    fun onProgressFinish()
}