package com.posapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.posapplication.Util.Const
import com.posapplication.R
import com.posapplication.Util.Utils
import com.posapplication.databinding.FragmentCreateProfileBinding
import com.posapplication.ui.Activity.MainActivity

class CreateProfileFragment : BaseFragment()
{
    var cardno = ""
    var location = 0.0
    var deviceno = ""
    var email = ""

    var binding: FragmentCreateProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).actionBar(getString(R.string.create_profile), isActionBar = true, isBack = false)
        if (binding == null) binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_profile, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI()
    {
        cardno= Const.CREDIT_CARD_NO
        var lat =   Utils.getLatitudeLocation(requireContext(), "latitude")
        var lont =    Utils.getLongitudeLocation(requireContext(), "longitude")

        binding?.cardno?.setText("Card No : $cardno")
        binding?.location?.setText("Lat : $lat , Long : $lont")
        binding?.deviceno?.setText("Device Number : 01e1e4eb97ccee24")
        binding?.email?.setText("Email : aa@outlook.com")

    }

}