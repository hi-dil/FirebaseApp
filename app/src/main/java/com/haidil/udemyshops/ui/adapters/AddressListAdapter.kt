package com.haidil.udemyshops.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ItemAddressLayoutBinding
import com.haidil.udemyshops.model.Address
import com.haidil.udemyshops.ui.activities.AddEditAddressActivity
import com.haidil.udemyshops.ui.activities.CheckoutActivity
import com.haidil.udemyshops.util.Constants

open class AddressListAdapter(
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemAddressLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.binding.tvAddressFullName.text = model.name
            holder.binding.tvAddressType.text = model.type
            holder.binding.tvAddressDetails.text = holder.itemView.resources.getString(
                R.string.bind_formatted_address_details,
                model.address,
                model.zipCode
            )

            holder.binding.tvAddressMobileNumber.text = holder.itemView.resources.getString(
                R.string.bind_formatted_mobileNumber,
                model.mobileNumber
            )

            if (selectAddress) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, model)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESSES_DETAIL, list[position])
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }

    class MyViewHolder(val binding: ItemAddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}