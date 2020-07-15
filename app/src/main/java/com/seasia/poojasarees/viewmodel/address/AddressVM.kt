package com.seasia.poojasarees.viewmodel.address

import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.constants.AppConstants
import com.seasia.poojasarees.model.helper.Address
import com.seasia.poojasarees.model.request.AddressIn
import com.seasia.poojasarees.model.response.AddressOut
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.repository.address.AddressRepo
import com.seasia.poojasarees.utils.PreferenceKeys

class AddressVM : ViewModel() {
    private var addressRepo: AddressRepo
    private var addOrUpdateAddressData: MutableLiveData<AddressOut>
    private var allStatesData: MutableLiveData<ArrayList<AllStatesOut>>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        addressRepo = AddressRepo()
        addOrUpdateAddressData = MutableLiveData<AddressOut>()
        allStatesData = MutableLiveData<ArrayList<AllStatesOut>>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        isLoading.postValue(false)
        allStatesData = addressRepo.allStatesResponse(false)
        addressRepo.addOrUpdateAddress("", null, false)
    }

    fun onUpdateClick(view: View, model: Address) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val shop = model.shopName
            val street = model.street
            val town = model.town
            val district = model.district
            val state = model.state
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

            if (state.isEmpty()) {
                userMsg.postValue(SELECT_STATE)
                return
            }

            val addressIn = AddressIn()
            val custId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""
            addressIn.customer.id = if (!custId.isEmpty()) custId.toInt() else "0".toInt()
            addressIn.customer.group_id = AppConstants.GROUP_ID.toInt()
            addressIn.customer.email = MyApplication.sharedPref.getString(PreferenceKeys.EMAIL, "") ?: ""
            addressIn.customer.firstname = MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME, "") ?: ""
            addressIn.customer.lastname = MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME, "") ?: ""

            val address = MyApplication.sharedPref.getString(PreferenceKeys.USER_ALL_ADDRESS, "") ?: ""
            addressIn.customer.addresses = UtilsFunctions.getUserAddress(address)

            addOrUpdateAddressData = addressRepo.addOrUpdateAddress(custId, addressIn, true)
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

    fun getAllStatesResponse(): LiveData<ArrayList<AllStatesOut>> {
        return allStatesData
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