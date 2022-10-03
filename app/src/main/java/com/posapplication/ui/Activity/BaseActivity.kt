package com.posapplication.ui.Activity


import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ktx.BuildConfig
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.google.gson.GsonBuilder
import com.posapplication.R
import com.posapplication.Util.ClientInterface
import com.posapplication.Util.Const
import com.posapplication.Util.ServiceBuilder
import com.posapplication.Util.VolleyResponseListener
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


open class BaseActivity : AppCompatActivity(), VolleyResponseListener {

    var progressBar: ProgressDialog ? = null
    var dialog: Dialog? = null
    var inflater: LayoutInflater? = null
    var isLoad = false
    open var permCallback: PermCallback? = null
    var progressDialog: Dialog? = null
    val txtMsgTV: TextView? = null
    var reqCode = 0
    val networkAlertDialog: AlertDialog? = null
    val networkStatus: String? = null
    var inputMethodManager: InputMethodManager? = null
    var failureDailog: android.app.AlertDialog.Builder? = null
    var exit = false
    private val networksBroadcast: NetworksBroadcast? = null
//    var store: PrefStore? = null
    var resultIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inputMethodManager = this
                .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        initializeNetworkBroadcast()
        strictModeThread()

        failureDailog = android.app.AlertDialog.Builder(this)
    }

    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }
    }



    fun showDialogLoader() {

        if (dialog == null || !dialog!!.isShowing) {
            dialog = Dialog(this, R.style.progress_dialog)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setCancelable(false)
            dialog!!.setContentView(R.layout.dialog_loader)
            dialog!!.show()
        }

    }

    fun isEmailValidate(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getStringText(text: Int) {
        this.resources.getString(text)
    }

    open fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }




    fun initializeNetworkBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        //        networksBroadcast = new NetworksBroadcast();
        registerReceiver(networksBroadcast, intentFilter)
    }

    fun strictModeThread() {
        val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    open fun sendNotification() {

    }
    fun dismissDialogLoader()
    {
        try
        {
            if (dialog != null && dialog!!.isShowing)
            {
                dialog!!.dismiss()
            }
        }
        catch (e: IllegalArgumentException)
        {
            e.printStackTrace()
        }
    }

    open fun getRetrofitobject(): ClientInterface
    {

        val gson = GsonBuilder()
            .setLenient()
            .create()!!

        val client = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(ServiceBuilder.loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        val service = retrofit.create(ClientInterface::class.java)

        return service

    }



    fun logs(string: String) {
        if (BuildConfig.DEBUG) {
            Log.e("BaseActivity", string)
        }
    }



    fun checkPermissions(perms: Array<String>, requestCode: Int, permCallback: PermCallback?): Boolean
    {
        this.permCallback = permCallback
        reqCode = requestCode
        val permsArray: MutableList<String> = ArrayList()
        var hasPerms = true
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                permsArray.add(perm)
                hasPerms = false
            }
        }
        if (!hasPerms) {
            val permsString = permsArray.toTypedArray()
            for (i in permsArray.indices) {
                permsString[i] = permsArray[i]
            }
            ActivityCompat.requestPermissions(this, permsString, 99)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permGrantedBool = false
        if (requestCode == 99) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                permGrantedBool = false
                return
            } else {
                permGrantedBool = true
            }
            if (permCallback != null)
            {
                if (permGrantedBool)
                {
                    permCallback!!.permGranted(reqCode)
                }
                else
                {
                    permCallback!!.permDenied(reqCode)
                }
            }
        }
    }



    interface PermCallback {
        fun permGranted(resultCode: Int)
        fun permDenied(resultCode: Int)
    }

    internal inner class NetworksBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

        }
    }

    fun showToastOne(msg: String?) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun log(string: String?) {
        if (BuildConfig.DEBUG) {
            Log.e("BaseActivity", string!!)
        }
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            if (currentFocus != null) {
                inputMethodManager!!.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
                return true
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
        return false
    }


    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    fun goToTapScreenActivity() {
        startActivity(Intent(this, TapscreenTestActivity::class.java))
        finish()
    }

    fun goToOptionActivity()
    {
        startActivity(Intent(this, OptionActivity::class.java))
        finishAffinity()
    }




    fun hitApi(url: String, listener: VolleyResponseListener)
    {

        Log.e("url ", Const.BASE_URL + url)
        listener.onProgressStart()
        val stringRequest = StringRequest(
                Request.Method.GET,
                Const.BASE_URL + url, { response: String? ->
            listener.onProgressFinish()
            Log.e("Success ", response!!)
            try {
                val jsonObject = JSONObject(response)
                listener.onResponse(url, jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error: VolleyError? ->
            listener.onProgressFinish()
            listener.onError(url, error)
        }

        stringRequest.retryPolicy = object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return Const.API_TIMEOUT
            }

            override fun getCurrentRetryCount(): Int {
                return Const.API_TIMEOUT
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        }
    }


    override fun onError(url: String?, message: VolleyError?)
    {
        if (message is TimeoutError || message is NoConnectionError) {
            showToast("Timeout server error")
        } else if (message is AuthFailureError) {
            showToast("Authentication failed")
        } else if (message is ServerError) {
            showToast("Server error")
        } else if (message is NetworkError) {
            showToast("Network error")
        } else if (message is ParseError) {
            showToast("Parsing error")
        }
    }



    override fun onResponse(url: String?, response: JSONObject?) {
    }

    override fun onProgressStart() {
        if (!isLoad) {
            startProgressDialog()
        }
        isLoad = false
    }


    fun showError(error: String) {
        android.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error)
                .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int -> }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }


    override fun onProgressFinish() {
        stopProgressDialog()
    }



    fun getdeviceidR():String
    {
        var deviceId = ""
        val android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        deviceId = android_id


        return deviceId

    }

    fun getdeviceid():String
    {
        var deviceId = ""
        val sm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            SubscriptionManager.from(this)
        }
        else
        {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        @SuppressLint("MissingPermission") val sis = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            sm.activeSubscriptionInfoList
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        val si = sis[0]
       deviceId = si.iccId

        return deviceId
    }


    open fun getSN(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Build.getSerial()

        }
        else Build.SERIAL
    }


    open fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction
        val fragmentManager = supportFragmentManager
        try {
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.addToBackStack(fragment.javaClass.name)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun replaceFragmentWithoutStack(fragment: Fragment?) {
        val fragmentTransaction: FragmentTransaction
        val fragmentManager = supportFragmentManager
        try {
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment!!)
            fragmentTransaction.commit()
        } catch (e: Exception) {
        }
    }

    fun showProgressDialog(activity: Context?) {
        if (progressBar == null) {
            progressBar = ProgressDialog(activity,R.style.AppCompatAlertDialogStyle)
            progressBar!!.setMessage("Please wait while loading Card Details...")
            progressBar!!.setCancelable(false)
            progressBar!!.show()
        }
    }

        fun stopProgresDialog()
            {
                if(progressBar!=null && progressBar!!.isShowing){
                    progressBar!!.dismiss()
                }
            }


}