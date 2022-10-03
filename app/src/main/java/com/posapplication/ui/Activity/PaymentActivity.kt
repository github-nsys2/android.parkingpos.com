package com.posapplication.ui.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil

import com.checkout.android_sdk.PaymentForm

import com.checkout.android_sdk.Response.CardTokenisationFail
import com.checkout.android_sdk.Response.CardTokenisationResponse
import com.google.android.gms.location.*
import com.google.gson.JsonObject
import com.posapplication.R
import com.posapplication.Util.ApiInterface
import com.posapplication.Util.Const
import com.posapplication.Util.ServiceBuilder
import com.posapplication.Util.Utils
import com.posapplication.databinding.ActivityPaymentBinding
import com.posapplication.databinding.DialogConfirmPaymentBinding
import com.posapplication.databinding.DialogDonePaymentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException



class PaymentActivity : BaseActivity() {
    var token = " "
    var amount = ""
    var deviceid= ""
    var email= ""
    var dialogBuilder: AlertDialog.Builder? = null
    var emailcontent:String? = null
    var alertDialog: AlertDialog? = null
    var dialogConfirmPaymentBinding: DialogConfirmPaymentBinding? = null
    var dialogDonePaymentBinding : DialogDonePaymentBinding? = null
    var binding:ActivityPaymentBinding? =null
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)

//                binding?.paymentForm?.setFormListener(mFormListener)
//                ?.set3DSListener(m3DSecureListener)
//                ?.includeBilling(false)
//                ?.setEnvironment(Environment.SANDBOX)
//                ?.setKey(Const.PUBLIC_KEY)


         amount = intent.getStringExtra(Const.KEY_AMOUNT)!!
         deviceid = intent.getStringExtra(Const.DEVICE_ID)!!
         email = intent.getStringExtra(Const.EMAIL_ID)!!

        if(Const.validtxn)
        {
//            HitsendemailApi(email,deviceid, amount!!,"")
//            binding?.titleTV?.text= "Valid Transaction"
//            showDialogBox()
        }

        else
        {
            if(Const.invalidlocation)
            {
                binding?.titleTV?.text=
                    "Invalid Location"
                Toast.makeText(this@PaymentActivity,R.string.invalid_location,Toast.LENGTH_LONG).show()
            }

            else if(Const.invaliddevice)
            {
                binding?.titleTV?.text= "Invalid Pos device"
                Toast.makeText(this@PaymentActivity,R.string.invalid_posdevice,Toast.LENGTH_LONG).show()
            }

            else if(Const.invalidcard)
            {
                binding?.titleTV?.text= "Invalid Card"
                Toast.makeText(this@PaymentActivity,R.string.invalid_card,Toast.LENGTH_LONG).show()
            }
        }
    }


//        latitude= Utils.getLatitudeLocation(this, "latitude")
//        longitude= Utils.getLongitudeLocation(this, "longitude")
//
//        if(longitude.equals(0.0) && latitude.equals(0.0))
//        {
//            requestLocationUpdates()
//        }
//        var cardDetailsView: CardDetailsView = CardDetailsView(this)


//        cardDetailsView.findViewById<CvvInput>(com.checkout.android_sdk.R.id.cvv_input).inputType=InputType.TYPE_NUMBER_VARIATION_PASSWORD



//        CardInputPresenter.CardInputUiState("11111111")
//
//
//
//        email = intent.getStringExtra(Const.EMAIL_ID)!!





    val m3DSecureListener: PaymentForm.On3DSFinished = object : PaymentForm.On3DSFinished {
        override fun onSuccess(token: String) {
            showToast(token)
        }

        override fun onError(errorMessage: String) {
            dismissDialogLoader()
            showToast(errorMessage)
        }
    }





    val mFormListener: PaymentForm.PaymentFormCallback = object : PaymentForm.PaymentFormCallback {
        override fun onFormSubmit() {
            showDialogLoader()
        }

        override fun onTokenGenerated(response: CardTokenisationResponse)
        {
            dismissDialogLoader()
            log("token " + response.token)
            token = (response.token)
            Log.e("token",token)
            Log.e("amount",amount)
            if (alertDialog == null)
            {

                if(Const.validtxn)
                {
                    showDialogBox()
                }
                else
                {
                    if(Const.invalidlocation)
                    {
                        Toast.makeText(this@PaymentActivity,R.string.invalid_location,Toast.LENGTH_LONG).show()
                    }

                    else if(Const.invaliddevice)
                    {
                        Toast.makeText(this@PaymentActivity,R.string.invalid_posdevice,Toast.LENGTH_LONG).show()
                    }

                    else if(Const.invalidcard)
                    {
                        Toast.makeText(this@PaymentActivity,R.string.invalid_card,Toast.LENGTH_LONG).show()
                    }

                }

            }

        }

        override fun onError(response: CardTokenisationFail) {
            dismissDialogLoader()
            showToast(response.errorType)
        }

        override fun onNetworkError(error: com.checkout.android_sdk.network.NetworkError?) {
            dismissDialogLoader()
            showToast(error.toString())
        }

        override fun onBackPressed() {
            val intent = Intent(this@PaymentActivity, OptionActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    @SuppressLint("SetTextI18n")
    fun showDialogBox()
    {
        dialogBuilder = AlertDialog.Builder(this)
        dialogConfirmPaymentBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
            R.layout.dialog_confirm_payment, null, false)
        dialogBuilder!!.setView(dialogConfirmPaymentBinding!!.getRoot())
        dialogConfirmPaymentBinding!!.amountTV.text = resources.getString(R.string.currency_symbol) + " " + intent.getStringExtra(
            Const.KEY_AMOUNT
        )
        alertDialog = dialogBuilder!!.create()
        alertDialog!!.show()
        alertDialog!!.setCancelable(false)


        dialogConfirmPaymentBinding!!.cancelTV.setOnClickListener {
            alertDialog!!.dismiss()
            alertDialog = null
        }

        dialogConfirmPaymentBinding!!.saveTV.setOnClickListener {

            showDialogLoader()
            hitpaymentapi(token,amount,deviceid)
        }
    }

    fun showPaymentDone()
    {
        dialogBuilder = AlertDialog.Builder(this)
        dialogDonePaymentBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
            R.layout.dialog_done_payment, null, false)
        dialogBuilder!!.setView(dialogDonePaymentBinding!!.getRoot())
        alertDialog = dialogBuilder!!.create()
        alertDialog!!.show()
        alertDialog!!.setCancelable(false)

        dialogDonePaymentBinding!!.okTV.setOnClickListener {
            alertDialog!!.dismiss()
            alertDialog = null

        }
    }


    fun hitpaymentapi(token: String, amount: String, deviceid: String)
    {
        val service =getRetrofitobject()
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.paymentApi(
                token,amount,deviceid)
            withContext(Dispatchers.Main)
        {

                dismissDialogLoader()
            try
            {
                if (response.isSuccessful)
                {
                    val responseData = response.body()
                    if(responseData?.success==true)
                    {
                        if (responseData?.approved == true && responseData?.status.equals("Authorized") )
                        {
                        val intent = Intent(this@PaymentActivity, MainActivity::class.java)
                        intent.putExtra(Const.KEY_PROFILE, true)
                        intent.putExtra(Const.KEY_TOTAL_AMOUNT, responseData.amount)
                        intent.putExtra(Const.KEY_CARD_TYPE, responseData!!.cardData!!.scheme)
                        intent.putExtra(Const.KEY_CARD_NAME, responseData.cardData!!.cardType)
                        intent.putExtra(Const.KEY_CARD_NUM, responseData.cardData!!.last4)
                        intent.putExtra(Const.KEY_REFRENCE, responseData.id)
                        intent.putExtra(Const.KEY_CURRENCY, responseData.currency)
                        intent.putExtra(Const.KEY_PAYMENT_STATUS, responseData.responseSummary)
                        showPaymentDone()
                        HitsendemailApi(email,deviceid, amount!!,"")
                        startActivity(intent)
                        finishAffinity()
                        Log.e("checkoutsucess", responseData?.message.toString())
                        }
                        else
                        {
                            Log.e("checkouterror", responseData?.message.toString())
                        alertDialog!!.dismiss()
                        showToast("please try again.")
                        }
                    }
                    else
                    {  alertDialog!!.dismiss()
                        Log.e("checkouterror", responseData?.message.toString())
                            HitsendemailApi(email,deviceid, amount!!,"")
                            showPaymentDone()
                        //    showError(responseData?.message.toString())
                    }

                }
                else
                {

                        alertDialog!!.dismiss()
                        showToast("something went wrong again.")
                }
            }
            catch (e: Exception)
            {
                    alertDialog!!.dismiss()
                    e.printStackTrace()
            }
        }
      }
    }


    private fun HitsendemailApi(
        emaillist: String? = null,
        Posid: String,
        task: String,
        emailmember: String)
    {



   emailcontent =
       Utils.CreateMailContent("Dupe", Posid, task, "10", longitude, latitude,"")

        var jsonObject = JsonObject()
        jsonObject.addProperty("recepientEmail", emaillist)
        jsonObject.addProperty("messageBody", emailcontent)
        jsonObject.addProperty("subject", task)
        jsonObject.addProperty("cc", "")

//        var emailcontent : String =   Emailcontent.CreateMailContent(name,task)

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response =  apiInterface.sendemail(jsonObject)
            withContext(Dispatchers.Main) {
                try {
                    if (response != null)
                    {
                        dismissDialogLoader()
                        if (response.isSuccessful)
                        {
                            if(response.body()?.success == true)
                            {
                                Toast.makeText(applicationContext,response.body()?.data, Toast.LENGTH_LONG).show()
                            }
                        }

                        else
                        {

                            Toast.makeText(applicationContext,response.body()?.data, Toast.LENGTH_LONG).show()
                        }

                    }
                }

                catch (e: JSONException)
                {
                    dismissDialogLoader()
                    Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun requestLocationUpdates()
    {
        val request = LocationRequest()
        request.setInterval(5000)
        request.setFastestInterval(5000)
        request.smallestDisplacement = 1.0f
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val client: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) { // Request location updates and when an update is
            // received, store the location in Firebase or server
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location = locationResult.getLastLocation()
                    if (location != null && (latitude != location.latitude || longitude !=location.longitude)) {
                        latitude = location.latitude
                        longitude = location.longitude
                        Utils.setLatitudeLocation(this@PaymentActivity, "latitude", latitude)
                        Utils.setLongitudeLocation(this@PaymentActivity, "longitude", longitude)

                    }
                }
            }, null)
        }
    }
}


