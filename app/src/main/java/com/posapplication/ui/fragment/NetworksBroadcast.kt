package com.posapplication.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast


class NetworksBroadcast: BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {


        try {
            if (isOnline(context)) {

//                dialog(true)
                Toast.makeText(context, "Online Connect Intenet", Toast.LENGTH_LONG).show()
                Log.e("broadcast", "Online Connect Intenet ")
            } else {
//                dialog(false)
                Log.e("broadcast", "No Internet Connection !!! ")
                Toast.makeText(context, "Conectivity Failure !!!", Toast.LENGTH_LONG).show()


            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


//    open fun dialog(value: Boolean) {
//        if (value) {
//
//            tv_check_connection?.setText("We are back !!!")
//            tv_check_connection?.setBackgroundColor(Color.GREEN)
//            tv_check_connection?.setTextColor(Color.WHITE)
//            val handler = Handler()
//            val delayrunnable = Runnable { tv_check_connection?.setVisibility(View.GONE) }
//            handler.postDelayed(delayrunnable, 3000)
//        } else {
//            tv_check_connection?.setVisibility(View.VISIBLE)
//            tv_check_connection?.setText("Could not Connect to internet")
//            tv_check_connection?.setBackgroundColor(Color.RED)
//            tv_check_connection?.setTextColor(Color.WHITE)
//        }
//    }


    fun isOnline(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }
    }
}