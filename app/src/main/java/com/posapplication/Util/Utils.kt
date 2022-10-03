package com.posapplication.Util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.text.htmlEncode

object Utils {

    val PREFS_NAME = "appname_prefs"

    fun CreateMailContent(
        name: String,
        posid: String,
        amount: String?,
        cashback: String,
        longitude: Double,
        latitude: Double,
        creditCardNo: String
    ): String
    {
        val encoded: String ="<html>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/action_page.php\">\n" +
                "  <label for=\"fname\">Hello $name</label><br>\n" +
                "  \n" +
                "  <label for=\"fname\">The following transaction has been processed on your card</label><br>\n" +
                "  \n" +
                "<br>\n" +
                "  \n" +
                "  <label for=\"fname\">Invoice Amount:    $amount</label><br>\n" +
                "    <label for=\"fname\">Cashback(10%):   $cashback</label><br>\n" +
                "    <label for=\"fname\">POS ID:          $posid</label><br>\n" +
                "    <label for=\"fname\">Longitude:       $longitude</label><br>\n" +
                "    <label for=\"fname\">Latitude:        $latitude</label><br>\n" +
                "    <label for=\"fname\">Card Number:     $creditCardNo</label><br>\n" +
                "    <label for=\"fname\">Ref: ERP Team</label><br>\n" +
                "\n" +
                "</form> \n" +
                "\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>"+
                "\n".htmlEncode()

        return encoded

    }


    fun isMyServiceRunning(serviceClass: Class<*>, mActivity: Activity): Boolean {
        val manager: ActivityManager =
            mActivity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.getClassName()) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    fun setPOSdeviceid(context : Context ,key: String, value: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }


    fun getPOSdeviceid(context : Context ,key: String): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString(key, "").toString()
    }

    fun setLatitudeLocation(context : Context ,key: String, value: Double) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putDouble(key, value)
        editor.apply()
    }

    fun setLongitudeLocation(context : Context ,key: String, value: Double) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putDouble(key, value)
        editor.apply()
    }

    fun getLatitudeLocation(context : Context ,key: String): Double {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getDouble(key, 0.0)
    }

    fun getLongitudeLocation(context : Context ,key: String): Double {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getDouble(key, 0.0)
    }


    fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    fun SharedPreferences.getDouble(key: String, default: Double) =
        java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))
}