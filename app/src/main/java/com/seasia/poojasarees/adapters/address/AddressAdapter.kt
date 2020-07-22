package com.seasia.poojasarees.adapters.address

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.seasia.poojasarees.R
import com.seasia.poojasarees.databinding.RowAddressBinding
import com.seasia.poojasarees.model.AddressOut
import com.seasia.poojasarees.model.Addresses
import com.seasia.poojasarees.views.address.AddAddressActivity

class AddressAdapter(
    val context: Context,
    val addressList: ArrayList<Addresses>
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

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
