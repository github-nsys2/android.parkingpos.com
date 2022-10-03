package com.posapplication.ui.ModelClass

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
import com.posapplication.Util.Utils
import com.posapplication.databinding.FragmentValidatePosmachineBinding
import com.posapplication.ui.Activity.MainActivity
import com.posapplication.ui.fragment.BaseFragment
import com.posapplication.ui.fragment.NetworksBroadcast
import kotlinx.coroutines.*
import org.json.JSONException




class ValidatePOSMachine : BaseFragment()
{
    var optionchoose: String? =null
    private var strDeviceId: String = ""

    var networksBroadcast: NetworksBroadcast = NetworksBroadcast()

    var binding: FragmentValidatePosmachineBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).actionBar(getString(R.string.validation_screen), isActionBar = true, isBack = false)
        if (binding == null) binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_validate_posmachine, container, false)
        return binding!!.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        optionchoose = activity?.intent?.getStringExtra(Const.OPTION_CHOOSE)
        strDeviceId=  getSN()
        Utils.setPOSdeviceid(requireContext(), Const.POS_ID,strDeviceId)

        var lat =   Utils.getLatitudeLocation(requireContext(), "latitude")
        var long =    Utils.getLongitudeLocation(requireContext(), "longitude")


        if(networksBroadcast.isOnline(requireContext()))
        {
            baseActivity?.showDialogLoader()
            getvalidposid(strDeviceId, lat, long)
        }
        else
        {
            Toast.makeText(requireContext(),resources.getString(R.string.no_internet),Toast.LENGTH_SHORT).show()
        }

    }



    private fun getvalidposid(strDeviceId: String, lat: Double, long: Double)
    {
        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()

        }

        val apiInterface: ApiInterface = ServiceBuilder.buildService(ApiInterface::class.java)
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response =  apiInterface.getvalidposid(strDeviceId, lat.toString(), long.toString())
            withContext(Dispatchers.Main) {
                try {
                    baseActivity?.dismissDialogLoader()
                    if (response != null)
                    {

                        if (response.isSuccessful)
                        {
                            if(response.body()?.success == true)
                            {
                                baseActivity?.goToTapScreenActivity()

                            }
                            else
                            {
                                Toast.makeText(requireContext(),response.body()?.response.toString(), Toast.LENGTH_LONG).show()
                            }
                        }

                        else
                        {

                            Toast.makeText(requireContext(),response.body()?.response.toString(), Toast.LENGTH_LONG).show()
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

     fun getSN(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()


        }
        else Build.SERIAL
    }

}