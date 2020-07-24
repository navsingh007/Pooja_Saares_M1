package com.seasia.poojasarees.adapters.address

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.callbacks.OnAddressDelete
import com.seasia.poojasarees.databinding.RowAddressBinding
import com.seasia.poojasarees.model.AddressOut
import com.seasia.poojasarees.model.Addresses
import com.seasia.poojasarees.views.address.AddAddressActivity

class AddressAdapter(
    val context: Context,
    val addressList: ArrayList<Addresses>,
    val deleteAddressDelegate: OnAddressDelete
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    private var lastCheckedPos = -1
    private var lastChecked: RadioButton? = null

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_address,
            parent,
            false
        ) as RowAddressBinding
        return ViewHolder(binding.root, viewType, binding, context, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val address = addressList[position]

        val street = address.street?.get(0) ?: ""
        val town = address.city ?: ""
        val state = address.region?.region ?: ""
        val postcode = address.postcode ?: ""
        val completeAddress = "${street}, ${town}, ${state}, ${postcode}"

        holder.binding.tvAddress.text = completeAddress

        holder.binding.root.setOnClickListener {
            context.startActivity(
                Intent(context, AddAddressActivity::class.java)
                    .putExtra("editAddress", addressList[position])
            )
        }

        holder.binding.ivDeleteAddress.setOnClickListener {
            deleteAddressDelegate.onDeleteAddress(addressList.get(position).id)
        }


        // Default Address selection logic
        holder.binding.rbSelectedAddress.setTag(position)

        if (address.default_shipping && address.default_billing) {
            holder.binding.rbSelectedAddress.setChecked(true)
            holder.binding.rbSelectedAddress.setClickable(false)
        } else {
            holder.binding.rbSelectedAddress.setChecked(false)
            holder.binding.rbSelectedAddress.setClickable(true)
        }
        holder.binding.rbSelectedAddress.setOnClickListener { v ->
            val cb = v as RadioButton
            val clickedPos = cb.tag as Int

            if (cb.isChecked) {
                if (lastChecked != null) {
                    lastChecked?.isChecked = false
                }
                lastChecked = cb
                lastCheckedPos = clickedPos
            } else {
                lastChecked = null
            }

            // Update Shipping address status of selected address

/*            for ((i, address) in addressList.withIndex()) {
                if (i == position) {
                    address.default_shipping = true
                    address.default_billing = true
                } else {
                    address.default_shipping = false
                    address.default_billing = false
                }
            }*/
            deleteAddressDelegate.onDefaultAddress(addressList.get(position).id)
        }
    }

    override fun getItemCount(): Int {
        return addressList.count()
//        return 5
    }

    inner class ViewHolder
        (
        v: View, val viewType: Int,
        val binding: RowAddressBinding,
        context: Context,
        addressList: ArrayList<Addresses>
    ) : RecyclerView.ViewHolder(v)

}
