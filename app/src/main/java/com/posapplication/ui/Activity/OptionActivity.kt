package com.posapplication.ui.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.posapplication.R
import com.posapplication.Util.Const
import com.posapplication.Util.Utils
import com.posapplication.databinding.ActivityOptionBinding


class OptionActivity :  BaseActivity(), BaseActivity.PermCallback {


    private var strAppVersion: String = ""
//    private var strDeviceId: String = ""
    var binding: ActivityOptionBinding? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var client: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permCallback= this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_option)


        initUI()
//        strDeviceId=  getSN()
//        var lat =   Utils.getLatitudeLocation(this, "latitude")
//        var long =    Utils.getLongitudeLocation(this, "longitude")

//        Utils.setLatitudeLocation(this@OptionActivity, "latitude", 30.6988303)
//        Utils.setLongitudeLocation(this@OptionActivity, "longitude",  76.6915557)




//        getvalidposid(strDeviceId,lat,long)


        binding!!.StartPaymentTV.setOnClickListener(View.OnClickListener {

        latitude =   Utils.getLatitudeLocation(this, "latitude")
        longitude =    Utils.getLongitudeLocation(this, "longitude")

            if(latitude!=0.0 && longitude!=0.0)
            {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Const.OPTION_CHOOSE, "optionactivity")
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this,"Please wait getting your location",Toast.LENGTH_LONG).show()
            }


        })
//
//        binding!!.InvalidLocationTV.setOnClickListener(View.OnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            Const.validtxn= false
//            Const.invalidlocation= true
//            Const.invaliddevice= false
//            Const.invalidcard= false
//            startActivity(intent)
//
//
//        })
//
//        binding!!.InvalidposdeviceTV.setOnClickListener(View.OnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            Const.validtxn= false
//            Const.invalidlocation= false
//            Const.invaliddevice= true
//            Const.invalidcard= false
//            startActivity(intent)
//
//
//        })
//
//        binding!!.InvalidcardTV.setOnClickListener(View.OnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            Const.validtxn= false
//            Const.invalidlocation= false
//            Const.invaliddevice= false
//            Const.invalidcard= true
//            startActivity(intent)
//
//
//        })
//
//        binding!!.createProfile.setOnClickListener(View.OnClickListener {
//
//            strDeviceId=  getSN()
//            var lat =   Utils.getLatitudeLocation(this, "latitude")
//            var long =    Utils.getLongitudeLocation(this, "longitude")
//            getvalidposid(strDeviceId,lat,long)
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra(Const.OPTION_CHOOSE, "createprofile")
//            startActivity(intent)
//        })
    }

    private fun initUI()
    {
        client = LocationServices.getFusedLocationProviderClient(this)
        binding?.titleTV?.setText(resources.getString(R.string.start_payment))
        binding?.titleTV?.textSize = 19F
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                99,
                permCallback
            )

            return
        }
        else
        {
            getLocation()
            requestLocationUpdates()
        }


    }

    override fun getSN(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()


        }
        else Build.SERIAL
    }



//    private fun getvalidposid(strDeviceId: String, lat: Double, long: Double)
//    {
//
//
//        var call: Call<POSIDModelClass?>? = null
//        val apiInterface: ApiInterface = ServiceBuilder.buildServicesec(ApiInterface::class.java)
//        call = apiInterface.getvalidposidcall(strDeviceId, lat.toString(), long.toString())
//
//        if(call != null)
//        {
//            call.enqueue(object : Callback<POSIDModelClass?> {
//                override fun onResponse(call: Call<POSIDModelClass?>, response: Response<POSIDModelClass?>)
//                {
//
//                    dismissDialogLoader()
//                    if(response.body()?.success?.equals("true") == true)
//                    {
//
//                    }
//
//                    else
//                    {
//
//                    }
//                }
//
//                override fun onFailure(call: Call<POSIDModelClass?>, t: Throwable)
//                {
//                    Toast.makeText(this@OptionActivity,t.toString(), Toast.LENGTH_LONG).show()
//                }
//            })
//        }
//    }




//    private fun getvalidposid(strDeviceId: String, lat: Double, long: Double)
//    {
//
//
//        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
//        CoroutineScope(Dispatchers.IO).launch {
//            val response =  apiInterface.getvalidposid(strDeviceId, lat.toString(), long.toString())
//            withContext(Dispatchers.Main) {
//                try {
//                    dismissDialogLoader()
//                    if (response != null)
//                    {
//
//                        if (response.isSuccessful)
//                        {
//                            if(response.body()?.success == true)
//                            {
//
//                                val intent = Intent(applicationContext, MainActivity::class.java)
//                                 Const.validtxn= true
//                                 Const.invalidlocation= false
//                                 Const.invaliddevice= false
//                                 Const.invalidcard= false
//                                 startActivity(intent)
//                            }
//                            else
//                            {
//                                Toast.makeText(applicationContext,response.body()?.response.toString(), Toast.LENGTH_LONG).show()
//                            }
//                        }
//
//                        else
//                        {
//
//                            Toast.makeText(applicationContext,response.body()?.response.toString(), Toast.LENGTH_LONG).show()
//                        }
//
//                    }
//                    else
//                    {
//
//                        Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_LONG).show()
//                    }
//
//                }
//
//                catch (e: JSONException)
//                {
////                    dismissDialogLoader()
//                    Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_LONG).show()
//                    e.printStackTrace()
//                }
//            }
//        }
//    }



    private fun requestLocationUpdates()
    {


        val request = LocationRequest()
        request.setInterval(5000)
        request.setFastestInterval(5000)
        request.smallestDisplacement = 1.0f
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED)
        { // Request location updates and when an update is
            // received, store the location in Firebase or server

            client.requestLocationUpdates(request, object : LocationCallback()
            {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location = locationResult.getLastLocation()
                    if (location != null && (latitude != location.latitude || longitude !=location.longitude))
                    {
                        latitude = location.latitude
                        longitude = location.longitude
                        Utils.setLatitudeLocation(this@OptionActivity, "latitude", latitude)
                        Utils.setLongitudeLocation(this@OptionActivity, "longitude", longitude)
                        binding?.StartPaymentTV?.visibility=View.VISIBLE
//                        Toast.makeText(this@OptionActivity,"latitude $latitude",Toast.LENGTH_LONG).show()
//                        Toast.makeText(this@OptionActivity,"longitude $longitude",Toast.LENGTH_LONG).show()
//                        Log.e("latitude", latitude.toString())
//                        Log.e("longitude", longitude.toString())

                    }
                }
            }, null)
        }
    }

    private fun getLocation()
    {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED)
        { // Request location updates and when an update is
            // received, store the location in Firebase or server

            client.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        Utils.setLatitudeLocation(this@OptionActivity, "latitude", latitude)
                        Utils.setLongitudeLocation(this@OptionActivity, "longitude", longitude)
                    }
                }
        }
    }


    override fun permGranted(resultCode: Int)
    {
        getLocation()
        requestLocationUpdates()
    }

    override fun permDenied(resultCode: Int) {

    }


}