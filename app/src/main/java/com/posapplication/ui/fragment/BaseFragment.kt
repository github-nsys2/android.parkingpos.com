package com.posapplication.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.posapplication.ui.Activity.BaseActivity

open class BaseFragment : Fragment(), AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener
{
    @JvmField
    var baseActivity: BaseActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity?

    }

    override fun onResume() {
        super.onResume()
        baseActivity!!.hideSoftKeyboard()
        requireActivity().invalidateOptionsMenu()
    }

    override fun onClick(view: View) {}
    override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {}

//    open fun replaceFragment(fragment: Fragment) {
//        val fragmentTransaction: FragmentTransaction
//        val fragmentManager = activity?.supportFragmentManager
//        try {
//            fragmentTransaction = fragmentManager!!?.beginTransaction()!!
//            fragmentTransaction.replace(R.id.container, fragment)
//            fragmentTransaction.addToBackStack(fragment.javaClass.name)
//            fragmentTransaction.commit()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//
//    fun replaceFragment(fragment: Fragment, bundle: Bundle?) {
//        val fragmentTransaction: FragmentTransaction
//        val fragmentManager = activity?.supportFragmentManager
//        try {
//            fragment.arguments = bundle
//            fragmentTransaction = fragmentManager!!?.beginTransaction()!!
//            fragmentTransaction.replace(R.id.container, fragment)
//            fragmentTransaction.addToBackStack(fragment.javaClass.name)
//            fragmentTransaction.commit()
//        } catch (e: Exception) {
//        }
//    }

}