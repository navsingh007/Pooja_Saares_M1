package com.seasia.poojasarees.views.address

import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.reflect.TypeToken
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityAddAddressBinding
import com.seasia.poojasarees.helper.AddAddressHelper
import com.seasia.poojasarees.model.helper.Address
import com.seasia.poojasarees.model.response.AddressOut
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.address.AddressVM
import com.tiper.MaterialSpinner

class AddAddressActivity : BaseActivity() {
    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var addressVM: AddressVM
    private var address: Address? = null
    private var allStatesInCountry = ArrayList<String>()
    private var allStatesAdapter: ArrayAdapter<String>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_add_address
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityAddAddressBinding

        addressVM = ViewModelProvider(this).get(AddressVM::class.java)
        address = Address()
        binding.address = address
        binding.addressVM = addressVM

        getExtras()
//        binding.btnCancel.setOnClickListener { finish() }
        showApiMsgObserver()
        showUserWarningObserver()
        allStatesObserver()
        addAddressObserver()
        getAllStatesIfNotPresentLocally()
        setStatesAdapter()
    }

    private fun setStatesAdapter() {
        allStatesAdapter = ArrayAdapter<String>(
            this,
            R.layout.row_spinner,
            allStatesInCountry
        )
        binding.spnState.adapter = allStatesAdapter
    }

    private fun getAllStatesIfNotPresentLocally() {
        binding.spnState.onItemSelectedListener = spinnerItemSelection
        val states = MyApplication.sharedPref.getString(PreferenceKeys.ALL_STATES, "") ?: ""

        if (!states.isEmpty()) {
            // All states locally
            val myType = object : TypeToken<ArrayList<AllStatesOut>>() {}.type
            val allStates = MyApplication.gson.fromJson<ArrayList<AllStatesOut>>(states, myType)

            val allStatesOut = allStates[0]
            val stateNames = ArrayList<String>()
            for (i in 0 until stateNames.size) {
                stateNames.add(allStatesOut.available_regions!![i].name!!)
            }
            allStatesInCountry.addAll(stateNames)
        } else {
            // All states from API
            addressVM.getAllStates()
        }
    }

    val spinnerItemSelection = object : MaterialSpinner.OnItemSelectedListener {
        override fun onItemSelected(
            parent: MaterialSpinner,
            view: View?,
            position: Int,
            id: Long
        ) {
            allStatesInCountry.let {
                address?.state = it[position]
            }
        }

        override fun onNothingSelected(parent: MaterialSpinner) {}
    }

    private fun getExtras() {
        val editAddress = intent.getSerializableExtra("editAddress")
        if (editAddress != null) {
            val editableAddress = editAddress as AddressOut.Addresse

            val city = editableAddress.city
            val pincode = editableAddress.postcode
            val street = editableAddress.street?.get(0) ?: ""
            val state = editableAddress.region?.region ?: ""

            address?.town = city ?: ""
            address?.pincode = pincode ?: ""
            address?.street = street
        }
    }

    private fun addAddressObserver() {
        addressVM.addOrUpdateAddressResponse().observe(this, Observer { address ->
            if (address != null) {
                // Update addresses locally
                MyApplication.sharedPref.save(PreferenceKeys.USER_ALL_ADDRESS, address.addresses)
            }
        })
    }

    private fun showApiMsgObserver() {
        addressVM.showApiMsg().observe(this, Observer { msg ->
            if (msg != null) {
                UtilsFunctions.showToastError(msg)
            }
        })
    }

    private fun showUserWarningObserver() {
        addressVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                AddAddressHelper.setUserMsg(this, msg)
            }
        })
    }

    private fun allStatesObserver() {
        addressVM.getAllStatesResponse().observe(this, Observer {
            if (!it.isEmpty() && it[0].available_regions != null) {
                val allStates = it[0].available_regions

                if (allStates != null) {
                    val stateNames = ArrayList<String>()
                    for (state in allStates) {
                        stateNames.add(state.name!!)
                    }
                    allStatesInCountry.clear()
                    allStatesInCountry.addAll(stateNames)
                    allStatesAdapter?.notifyDataSetChanged()
                }
            }
        })
    }
}