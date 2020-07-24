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
import com.seasia.poojasarees.helperlocalize.AddAddressHelper
import com.seasia.poojasarees.model.helper.Address
import com.seasia.poojasarees.model.AddressOut
import com.seasia.poojasarees.model.Addresses
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.authentication.LoginOut
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.address.AddressVM
import com.tiper.MaterialSpinner

class AddAddressActivity : BaseActivity() {
    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var addressVM: AddressVM
    private var address: Address? = null
    private var isUserUpdatingAddress = false
    private var city = ""
    private var state = ""

    // States
    private var allStatesInCountry = ArrayList<String>()
    private var allStatesAdapter: ArrayAdapter<String>? = null
    private var allStatesWithId: ArrayList<AllStatesOut.AvailableRegion> = ArrayList()

    //Towns
    private var townList: ArrayList<String> = ArrayList()
    private var townAdapter: ArrayAdapter<String>? = null
    private var allTownsWithIdList: ArrayList<AllTownsOut> = ArrayList()


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
        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()
        allStatesObserver()
        allTownObserver()
        addAddressObserver()
        getAllStatesIfNotPresentLocally()

        setStatesAdapter()
        setTownsAdapter()
    }

    private fun setStatesAdapter() {
        allStatesAdapter = ArrayAdapter<String>(
            this,
            R.layout.row_spinner,
            allStatesInCountry
        )
        binding.spnState.adapter = allStatesAdapter
    }

    private fun setTownsAdapter() {
        townAdapter = ArrayAdapter<String>(
            this,
            R.layout.row_spinner,
            townList
        )

        binding.spnTown.adapter = townAdapter
        binding.spnTown.onItemSelectedListener = spinnerItemSelection
    }

    private fun getAllStatesIfNotPresentLocally() {
        binding.spnState.onItemSelectedListener = spinnerItemSelection

        addressVM.getAllStates()

/*        val states = MyApplication.sharedPref.getString(PreferenceKeys.ALL_STATES, "") ?: ""

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
        }*/
    }

    val spinnerItemSelection = object : MaterialSpinner.OnItemSelectedListener {
        override fun onItemSelected(
            parent: MaterialSpinner,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (parent.id) {
                binding.spnTown.id -> {
                    val selectedCity = townAdapter?.getItem(position) ?: ""
                    var townId = ""
                    for (townWithId in allTownsWithIdList) {
                        if (selectedCity.equals(townWithId.value)) {
                            townId = townWithId.option_id ?: ""

//                            UtilsFunctions.showToastSuccess("townId - $townId")
                        }
                    }
                    address?.town = selectedCity
                    address?.townId = townId
                }

                binding.spnState.id -> {
                    allStatesWithId?.let {
                        address?.stateId = it[position].id ?: ""
                        address?.region = it[position].name ?: ""
                        address?.regionCode = it[position].code ?: ""
//                        UtilsFunctions.showToastSuccess("stateId - ${it[position].id}")
                    }

/*                    allStatesInCountry.let {
//                        address?.state = it[position]

                        val selectedState = allStatesAdapter?.getItem(position) ?: ""
                        val stateId = ""
                        for (stateWithId in allStatesWithId) {
                            if (selectedState.equals(stateWithId.available_regions.))
                        }
                    }*/
                }
            }
        }

        override fun onNothingSelected(parent: MaterialSpinner) {}
    }

    private fun getExtras() {
        val editAddress = intent.getSerializableExtra("editAddress")
        if (editAddress != null) {
            isUserUpdatingAddress = true

            val editableAddress = editAddress as Addresses

            city = editableAddress.city
            val pincode = editableAddress.postcode
            val street = editableAddress.street?.get(0) ?: ""
            state = editableAddress.region?.region ?: ""

            // City and State set in observer after value comes from API's

/*              var townCityId = ""

            // All custom attributes
          val rawUserObj = MyApplication.sharedPref.getString(PreferenceKeys.USER_OBJECT, "")
            val myType = object : TypeToken<LoginOut>() {}.type
            val userObj = MyApplication.gson.fromJson<LoginOut>(rawUserObj, myType)

            if (userObj.custom_attributes != null) {
                for (i in 0..userObj.custom_attributes.size - 1) {
                    val attributeCode = userObj.custom_attributes[i].attribute_code
//                                    var value = userObj.custom_attributes[i].value
                    if (attributeCode.equals("town_city_dropdown")) {
                        townCityId = userObj.custom_attributes[i].value ?: "0"
                        break
                    }
                }
            }*/


            address?.shopName = MyApplication.sharedPref.getString(PreferenceKeys.SHOP_NAME, "") ?: ""
//            address?.town = city ?: ""
            address?.pincode = pincode ?: ""
            address?.street = street

            // Set Updating/editable address ID and isEditable = true
            address?.isEditable = true
            address?.addressIdToEdit = editAddress.id
        }
    }

    private fun addAddressObserver() {
        addressVM.addOrUpdateAddressResponse().observe(this, Observer { address ->
            stopProgressDialog()

            if (address != null) {
                // Update addresses locally
                MyApplication.sharedPref.save(PreferenceKeys.USER_ALL_ADDRESS, address.addresses)
                UtilsFunctions.showToastSuccess(resources.getString(R.string.address_added_success))
                finish()
            }
        })
    }


    private fun loadingObserver() {
        addressVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        addressVM.showApiMsg().observe(this, Observer { msg ->
            stopProgressDialog()

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

                // Save all states locally
                MyApplication.sharedPref.save(PreferenceKeys.ALL_STATES, it)

                val allStates = it[0].available_regions

                if (allStates != null) {
                    // All states with ID
                    allStatesWithId = allStates

                    val stateNames = ArrayList<String>()
                    for (state in allStates) {
                        stateNames.add(state.name!!)
                    }
                    allStatesInCountry.clear()
                    allStatesInCountry.addAll(stateNames)
                    allStatesAdapter?.notifyDataSetChanged()


                    // Set already selected State in case, updating address
                    if (isUserUpdatingAddress) {
                        binding.spnState.selection = allStatesAdapter!!.getPosition(state)
                    }
                }
            }
        })
    }

    private fun allTownObserver() {
        addressVM.allTowns().observe(this, Observer { allTowns ->
            if (allTowns != null) {
                if (allTowns.size > 0) {
                    MyApplication.sharedPref.save(PreferenceKeys.ALL_TOWNS, allTowns)

                    // Town with ID list
                    allTownsWithIdList.clear()
                    allTownsWithIdList.addAll(allTowns)

                    val towns = ArrayList<String>()
                    for (town in allTowns) {
                        towns.add(town.value ?: "")
                    }

                    // Clear all previous data and add new
                    townList.clear()
                    townList.addAll(towns)
                    townAdapter?.notifyDataSetChanged()


                    // Set already selected Town in case, updating address
                    if (isUserUpdatingAddress) {
                        binding.spnTown.selection = townAdapter!!.getPosition(city)
                    }
                }
            }
        })
    }
}