package com.posapplication.Util

import android.app.Activity
import android.content.Context

object Prefs {

    fun getString(context: Context, key: String?, defValue: String?): String?
    {
        val preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        return preferences.getString(key, defValue)
    }

    fun putString(context: Context, key: String?, value: String?)
    {
        val preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        val prefsEditor = preferences.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }
}