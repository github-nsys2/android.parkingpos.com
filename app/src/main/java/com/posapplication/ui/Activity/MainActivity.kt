package com.posapplication.ui.Activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.posapplication.R
import com.posapplication.Util.Const
import com.posapplication.databinding.ActivityMainBinding
import com.posapplication.ui.fragment.CreateProfileFragment
import com.posapplication.ui.fragment.EnterAmountFragment
import com.posapplication.ui.ModelClass.ValidatePOSMachine
import com.posapplication.ui.fragment.VehicleFragment


class MainActivity : BaseActivity(){
    lateinit var mActivity: Activity
    var binding: ActivityMainBinding? = null
    val frag: Fragment? = null

    var fromactivity=""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (frag != null) supportFragmentManager.beginTransaction()
            .replace(R.id.container, frag)
            .commit()

        binding?.titleTV?.text = "Enter Details"
        binding?.titleTV?.textSize = 19F

        mActivity = this@MainActivity

      var  option = intent?.getStringExtra(Const.OPTION_CHOOSE)

        if(option.equals("createprofile"))
        {
            replaceFragment(CreateProfileFragment())
        }

        else if(option.equals("scanactivity"))
        {
            replaceFragment(VehicleFragment())
        }

        else if(option.equals("optionactivity"))
        {
            replaceFragment(ValidatePOSMachine())
        }

//        else if(option.equals("vehicle"))
//        {
//            replaceFragmentWithoutStack(VehicleFragment())
//        }
        else if(option.equals("redirecttooption"))
        {
            val myIntent = Intent(this, OptionActivity::class.java)
            startActivity(myIntent)
        }

        else
        {
//          replaceFragmentWithoutStack(TapCardFragment())
            val myIntent = Intent(this, TapscreenTestActivity::class.java)
            startActivity(myIntent)
        }




//        if(intent?.getStringExtra(Const.FROM_ACTIVITY)!=null)
//        {
//            fromactivity = intent?.getStringExtra(Const.FROM_ACTIVITY)?.toString()!!
//        }
//
//        if(fromactivity.equals("scanactivity"))
//        {
//
//
//        }
    }

    fun actionBar(title: String?, isActionBar: Boolean, isBack: Boolean)
    {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        binding!!.titleTV.text = title
        binding?.titleTV?.textSize = 19F
        if (!isActionBar) {
            binding!!.toolbar.visibility = View.GONE

        }
        else
        {
            binding!!.toolbar.visibility = View.VISIBLE

        }
    }

    override fun onBackPressed()
    {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        when(fragment) {
            is EnterAmountFragment -> {
                val myIntent = Intent(this, OptionActivity::class.java)
                startActivity(myIntent)
                finishAffinity()
            }
            is CreateProfileFragment -> {
                val myIntent = Intent(this, OptionActivity::class.java)
                startActivity(myIntent)
                finishAffinity()
            }

            is ValidatePOSMachine ->
            {
                val myIntent = Intent(this, OptionActivity::class.java)
                startActivity(myIntent)
                finishAffinity()
            }

            is VehicleFragment ->
            {
                val myIntent = Intent(this, TapscreenTestActivity::class.java)
                startActivity(myIntent)
                finishAffinity()
            }

            else ->
            {
                super.onBackPressed()
            }

        }


    }


}