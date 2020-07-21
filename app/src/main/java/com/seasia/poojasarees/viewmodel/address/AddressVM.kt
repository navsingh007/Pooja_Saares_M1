package com.seasia.poojasarees.viewmodel.address

import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.reflect.TypeToken
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.constants.AppConstants
import com.seasia.poojasarees.model.helper.Address
import com.seasia.poojasarees.model.request.AddressIn
import com.seasia.poojasarees.model.response.AddressOut
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.address.AddressByIdOut
import com.seasia.poojasarees.repository.address.AddressRepo
import com.seasia.poojasarees.utils.PreferenceKeys

class AddressVM : ViewModel() {
    private var addressRepo: AddressRepo
    private var addOrUpdateAddressData: MutableLiveData<AddressOut>
    private var deleteAddressData: MutableLiveData<Boolean>
    private var getAddressByIdData: MutableLiveData<AddressByIdOut>
    private var allStatesData: MutableLiveData<ArrayList<AllStatesOut>>
    private var allTownsData: MutableLiveData<ArrayList<AllTownsOut>>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        addressRepo = AddressRepo()
        addOrUpdateAddressData = MutableLiveData<AddressOut>()
        deleteAddressData = MutableLiveData<Boolean>()
        getAddressByIdData = MutableLiveData<AddressByIdOut>()
        allStatesData = MutableLiveData<ArrayList<AllStatesOut>>()
        allTownsData = MutableLiveData<ArrayList<AllTownsOut>>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        isLoading.postValue(false)
        allStatesData = addressRepo.allStatesResponse(false)
        addressRepo.addOrUpdateAddress("", null, false)
        addressRepo.deleteAddressById("", false)
        addressRepo.getAddressByID("", false)
        allTownsData = addressRepo.getAllTowns(true)
    }

    fun onAddAddress(view: View, model: Address) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val shop = model.shopName
            val street = model.street
            val town = model.town
            val district = model.district
            val stateId = model.state
            val pincode = model.pincode

            val userInputs = arrayOf(shop, street, town, district, pincode)
            val msg =
                arrayOf(ENTER_SHOP_NAME, ENTER_STREET, ENTER_TOWN, ENTER_DISTRICT, ENTER_PIN_CODE)

            for (i in 0 until userInputs.size) {
                if (TextUtils.isEmpty(userInputs[i])) {
                    userMsg.postValue(msg[i])
                    return
                }
            }

            val userMinInputs = arrayOf(shop, street, town, district)
            val minMsg = arrayOf(MIN_SHOP, MIN_STREET, MIN_TOWN, MIN_DISTRICT)

            for (i in 0 until userMinInputs.size) {
                if (userMinInputs[i].length < 3) {
                    userMsg.postValue(minMsg[i])
                    return
                }
            }

            if (pincode.length < 6) {
                userMsg.postValue(MIN_PINCODE)
                return
            }

            if (pincode.startsWith("0")) {
                userMsg.postValue(INVALID_PINCODE)
                return
            }

            if (stateId.isEmpty()) {
                userMsg.postValue(SELECT_STATE)
                return
            }

            val addressIn = AddressIn()
            val custId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""
            addressIn.customer.id = if (!custId.isEmpty()) custId.toInt() else "0".toInt()
            addressIn.customer.group_id = AppConstants.GROUP_ID.toInt()
            addressIn.customer.email =
                MyApplication.sharedPref.getString(PreferenceKeys.EMAIL, "") ?: ""
            addressIn.customer.firstname =
                MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME, "") ?: ""
            addressIn.customer.lastname =
                MyApplication.sharedPref.getString(PreferenceKeys.LAST_NAME, "") ?: ""
/*            val address =
                MyApplication.sharedPref.getString(PreferenceKeys.USER_ALL_ADDRESS, "") ?: ""

            if (!address.isEmpty()) {
                addressIn.customer.addresses = UtilsFunctions.getUserAddress(address)
            }*/

            // Create new address for new id - 0
            try {
                val custId =
                    MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""

                var customerId = 0
                if (!custId.isEmpty()) {
                    customerId = custId.toInt()
                }

                // Get selected state
                var allStates = arrayListOf<AllStatesOut>()
                val states = MyApplication.sharedPref.getString(PreferenceKeys.ALL_STATES, "") ?: ""
                if (!states.isEmpty()) {
                    val myType = object : TypeToken<ArrayList<AllStatesOut>>() {}.type
                    val rawStates =
                        MyApplication.gson.fromJson<ArrayList<AllStatesOut>>(states, myType)
                    allStates.addAll(rawStates)
                }

                var regionName = ""
                var regionCode = ""
                var regionId = 0
                val allRegions = allStates[0].available_regions!!
                for (region in allRegions) {
                    if (region.id.equals(stateId)) {
                        regionName = region.name ?: ""
                        regionCode = region.code ?: ""
                        regionId = region.id?.toInt() ?: 0
                    }
                }

                val region = AddressIn.Region(
                    region = regionName,
                    region_code = regionCode,
                    region_id = regionId
                )

                // Customer all saved addresses
                val savedAddress =
                    MyApplication.sharedPref.getString(PreferenceKeys.USER_ALL_ADDRESS, "") ?: ""
                if (!savedAddress.isEmpty()) {
                    val myType = object : TypeToken<ArrayList<AddressOut.Addresse>>() {}.type
                    val allAddresses = MyApplication.gson.fromJson<ArrayList<AddressIn.Addresse>>(
                        savedAddress,
                        myType
                    )

                    addressIn.customer.addresses.addAll(allAddresses)
                }

                val newAddress = AddressIn.Addresse(
                    city = town,
                    country_id = "IN",
                    customer_id = customerId,
                    firstname = MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME, "")
                        ?: "",
                    id = 0,
                    lastname = MyApplication.sharedPref.getString(PreferenceKeys.LAST_NAME, "")
                        ?: "",
                    postcode = pincode,
                    district = district,
                    region = region,
                    region_id = regionId,
                    street = arrayListOf(street),
                    telephone = MyApplication.sharedPref.getString(PreferenceKeys.PHONE_NO, "")
                        ?: ""
                )
                addressIn.customer.addresses.add(newAddress)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            addOrUpdateAddressData = addressRepo.addOrUpdateAddress(custId, addressIn, true)
            isLoading.postValue(true)
        }
    }

    fun deleteAddressById(addressId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            deleteAddressData = addressRepo.deleteAddressById(addressId, true)
            isLoading.postValue(true)
        }
    }

    fun getAddressById(addressId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            getAddressByIdData = addressRepo.getAddressByID(addressId, true)
            isLoading.postValue(true)
        }
    }

    fun getAllStates() {
        allStatesData = addressRepo.allStatesResponse(true)
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return addressRepo.isSessionExpire()
    }

    fun addOrUpdateAddressResponse(): LiveData<AddressOut> {
        return addOrUpdateAddressData
    }

    fun deleteAddressResponse(): LiveData<Boolean> {
        return deleteAddressData
    }

    fun getAddressByIdResponse(): LiveData<AddressByIdOut> {
        return getAddressByIdData
    }

    fun getAllStatesResponse(): LiveData<ArrayList<AllStatesOut>> {
        return allStatesData
    }

    fun allTowns(): LiveData<ArrayList<AllTownsOut>> {
        return allTownsData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return addressRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    companion object {
        val ENTER_SHOP_NAME = "enterShopName"
        val ENTER_STREET = "enterStreet"
        val ENTER_TOWN = "enterTown"
        val ENTER_DISTRICT = "enterDistrict"
        val ENTER_PIN_CODE = "enterPincode"

        val SELECT_STATE = "selectState"

        val MIN_SHOP = "minShop"
        val MIN_STREET = "minStreet"
        val MIN_TOWN = "minTown"
        val MIN_DISTRICT = "minDistrict"
        val MIN_PINCODE = "minPincode"

        val INVALID_PINCODE = "invalidPincode"
    }
}