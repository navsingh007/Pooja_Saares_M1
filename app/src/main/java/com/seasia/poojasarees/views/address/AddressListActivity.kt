package com.seasia.poojasarees.views.address

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seasia.poojasarees.R
import com.seasia.poojasarees.adapters.address.AddressAdapter
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityAddressListBinding
import com.seasia.poojasarees.model.response.AddressOut
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.address.AddressVM

class AddressListActivity : BaseActivity() {
    private lateinit var binding: ActivityAddressListBinding
    private lateinit var addressVM: AddressVM
    private val addressList = ArrayList<AddressOut.Addresse>()
    private var addressAdapter: AddressAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_address_list
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityAddressListBinding

        addressVM = ViewModelProvider(this).get(AddressVM::class.java)
        binding.addressVM = addressVM

        setAddressAdapter()
        updateAddressList()
        addAddress()
    }

    private fun addAddress() {
        binding.tvAddAddress.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
    }

    private fun updateAddressList() {
        val userAddress =
            MyApplication.sharedPref.getString(PreferenceKeys.USER_ALL_ADDRESS, "") ?: ""

        if (!userAddress.isEmpty()) {
            val myType = object : TypeToken<ArrayList<AddressOut.Addresse>>() {}.type
            val allAddresses = MyApplication.gson.fromJson<ArrayList<AddressOut.Addresse>>(userAddress, myType)

            addressList.clear()
            addressList.addAll(allAddresses)
            addressAdapter?.notifyDataSetChanged()

            binding.tvNoRecord.visibility = View.GONE
            binding.rvAddresses.visibility = View.VISIBLE
        } else {
            binding.tvNoRecord.visibility = View.VISIBLE
            binding.rvAddresses.visibility = View.GONE
        }
    }

    private fun setAddressAdapter() {
        addressAdapter = AddressAdapter(this, addressList)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.rvAddresses.layoutManager = linearLayoutManager
        binding.rvAddresses.adapter = addressAdapter
    }
}