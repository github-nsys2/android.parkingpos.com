package com.posapplication.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.posapplication.R
import com.posapplication.databinding.AdapterVehicleBinding
import com.posapplication.ui.Activity.BaseActivity
import java.util.ArrayList


class VehicleAdapter(private val context: BaseActivity, var machineName: ArrayList<String>, private val onClickInterface: OnCLickInterface) : RecyclerView.Adapter<VehicleAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterVehicleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_vehicle, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.VehicleNo.text = machineName[position]


        holder.binding.cardCV.setOnClickListener(View.OnClickListener {
            onClickInterface.onAddClick(position)
        })

    }
    override fun getItemCount(): Int {
        // return 1
        return machineName.size
    }

    interface OnCLickInterface {
        fun onAddClick(position: Int)


    }

    inner class ViewHolder(var binding: AdapterVehicleBinding) : RecyclerView.ViewHolder(binding.root)


}