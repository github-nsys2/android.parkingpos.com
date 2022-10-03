package com.posapplication.ui.fragment


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.posapplication.*
import com.posapplication.Util.ApiInterface
import com.posapplication.Util.Const
import com.posapplication.Util.ServiceBuilder
import com.posapplication.databinding.FragmentEnterAmountBinding
import com.posapplication.ui.Activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

class EnterAmountFragment : BaseFragment()
{

    var email: String =""
    var amount: String =""
    var optionchoose: String? =null
    var deviceid: String? =null
    private var strDeviceId: String = ""

var isEmailValidate = false

var binding:FragmentEnterAmountBinding ? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).actionBar(getString(R.string.enter_detail), isActionBar = true, isBack = false)
        if (binding == null) binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_enter_amount, container, false)
        return binding!!.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        optionchoose = activity?.intent?.getStringExtra(Const.OPTION_CHOOSE)
        strDeviceId= "VS15183V02560"
//        var lat =   Utils.getLatitudeLocation(requireContext(), "latitude")
//        var long =    Utils.getLongitudeLocation(requireContext(), "longitude")

        var lat = 30.6988303
        var long = 76.6915557

//         deviceid =  getSN()
        deviceid =  "01e1e4eb97ccee24"
        var card_no = Const.CREDIT_CARD_NO
        binding?.cardnumber?.setText("Card No: $card_no")

        binding!!.submitBT.setOnClickListener(View.OnClickListener {
            amount = binding?.amountET?.text.toString()
            email =  binding!!.emailET.text.toString()
                if (validation())
                {
                    baseActivity?.showDialogLoader()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra(Const.OPTION_CHOOSE, "vehicle")
                    startActivity(intent)

//                    setcardvalue(strDeviceId,card_no,email,"",amount,lat,long)



//                    val intent = Intent(activity, VehicleListActivity::class.java)
//                    startActivity(intent)
//                    val intent = Intent(context, PaymentActivity::class.java)
//                    intent.putExtra(Const.KEY_AMOUNT, amount)
//                    intent.putExtra(Const.DEVICE_ID, deviceid)
//                    intent.putExtra(Const.EMAIL_ID, email)
//                    startActivity(intent)

                }

        })

    }

private fun validation(): Boolean
{


    var isValidate = true


    if (amount?.isEmpty() == true)
    {
        isValidate = false
        binding?.llErroramount?.visibility = View.VISIBLE

    }
    if (email?.isEmpty() == true)
    {
        isValidate = false
        binding?.llErrorEmail?.visibility = View.VISIBLE
    }
    else
    {
//        if (isEmailValidate(email.toString()))
//        {
//            isValidate = true
//        }
//        else
//        {
//            isValidate = false
//            binding?.llErrorEmail?.visibility = View.VISIBLE
//            binding?.tvErrorEmail?.text = "Invalid Email"
//        }
    }
    return isValidate

}


    private fun setcardvalue(strDeviceId: String,CardNumber :String,CustomerEmail :String,CustomerFullName :String,TransAmount :String, lat: Double, long: Double)
    {


        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response =  apiInterface.setcardvalue(strDeviceId,CardNumber,CustomerEmail,CustomerFullName,TransAmount, lat.toString(), long.toString())
            withContext(Dispatchers.Main) {
                try {
                    if (response != null)
                    {
                       baseActivity?.dismissDialogLoader()
                        if (response.isSuccessful)
                        {
                            if(response.body()?.success == true)
                            {
                                Toast.makeText(requireContext(),"Successfully uploded", Toast.LENGTH_LONG).show()

                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.putExtra(Const.OPTION_CHOOSE, "vehicle")
                                startActivity(intent)
                            }
                        }

                        else
                        {

                            Toast.makeText(requireContext(),"Error in uploding", Toast.LENGTH_LONG).show()
                        }

                    }
                    else
                    {

                        Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG).show()
                    }

                }

                catch (e: JSONException)
                {
//                    dismissDialogLoader()
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }

//    private fun isEmailValidate(toString: String): Boolean {
//        TODO("Not yet implemented")
//    }

     fun getSN(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()


        }
        else Build.SERIAL
    }
}