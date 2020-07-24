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
import com.seasia.poojasarees.model.*
import com.seasia.poojasarees.model.helper.Address
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.authentication.LoginOut
import com.seasia.poojasarees.repository.address.AddressRepo
import com.seasia.poojasarees.utils.PreferenceKeys

class AddressVM : ViewModel() {
    private var addressRepo: AddressRepo
    private var addOrUpdateAddressData: MutableLiveData<AddressOut>
    private var deleteOrDefaultAddressData: MutableLiveData<AddressOut>
    private var deleteAddressData: MutableLiveData<Boolean>
    private var getAddressByIdData: MutableLiveData<Addresses>
    private var allStatesData: MutableLiveData<ArrayList<AllStatesOut>>
    private var allTownsData: MutableLiveData<ArrayList<AllTownsOut>>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        addressRepo = AddressRepo()
        addOrUpdateAddressData = MutableLiveData<AddressOut>()
        deleteOrDefaultAddressData = MutableLiveData<AddressOut>()
        deleteAddressData = MutableLiveData<Boolean>()
        getAddressByIdData = MutableLiveData<Addresses>()
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
        addOrUpdateAddressData = addressRepo.addOrUpdateAddress("", null, false)
        deleteOrDefaultAddressData = addressRepo.addOrUpdateAddress("", null, false)
        deleteAddressData = addressRepo.deleteAddressById("", false)
        getAddressByIdData = addressRepo.getAddressByID("", false)
    }

    fun onAddAddress(view: View, model: Address) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val shop = model.shopName
            val street = model.street
            val townId = model.townId
            val town = model.town
            val district = model.district

            // Region
            val stateId = model.stateId
            val regionName = model.region
            val regionCode = model.regionCode

            val pincode = model.pincode

            val userInputs = arrayOf(shop, street, district, pincode)
            val msg =
                arrayOf(ENTER_SHOP_NAME, ENTER_STREET, ENTER_DISTRICT, ENTER_PIN_CODE)

            for (i in 0 until userInputs.size) {
                if (TextUtils.isEmpty(userInputs[i])) {
                    userMsg.postValue(msg[i])
                    return
                }
            }

            val userMinInputs = arrayOf(shop, street, district)
            val minMsg = arrayOf(MIN_SHOP, MIN_STREET, MIN_DISTRICT)

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
            addressIn.customer.store_id =
                UtilsFunctions.getLanguageStoreId(MyApplication.instance).toInt()
            addressIn.customer.website_id = AppConstants.WEBSITE_ID.toInt()
            addressIn.customer.disable_auto_group_change =
                AppConstants.DISABLE_AUTO_GROUP_CHANGE.toInt()

            // Create new address for new id - 0
            try {
                val custId =
                    MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""

                var customerId = 0
                if (!custId.isEmpty()) {
                    customerId = custId.toInt()
                }

                // Get selected state
/*                var allStates = arrayListOf<AllStatesOut>()
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
                }*/

                val region = Region(
                    region = regionName,
                    region_code = regionCode,
                    region_id = stateId.toInt()
                )

                // All customer addresses + New address (or Updated address)
                // Customer all saved addresses
                val savedAddress =
                    MyApplication.sharedPref.getString(PreferenceKeys.USER_ALL_ADDRESS, "") ?: ""
                if (!savedAddress.isEmpty()) {
                    val myType = object : TypeToken<ArrayList<Addresses>>() {}.type
                    val allAddresses = MyApplication.gson.fromJson<ArrayList<Addresses>>(
                        savedAddress,
                        myType
                    )
                    addressIn.customer.addresses.addAll(allAddresses)
                }

                // All custom attributes
                val rawUserObj = MyApplication.sharedPref.getString(PreferenceKeys.USER_OBJECT, "")
                val myType = object : TypeToken<LoginOut>() {}.type
                val userObj = MyApplication.gson.fromJson<LoginOut>(rawUserObj, myType)

                if (userObj.custom_attributes != null) {
                    for (i in 0..userObj.custom_attributes.size - 1) {
                        val attributeCode = userObj.custom_attributes[i].attribute_code
//                                    var value = userObj.custom_attributes[i].value
                        if (attributeCode.equals("shopname")) {
                            userObj.custom_attributes[i].value = shop
                            break
                        }
                    }
                }
                addressIn.customer.custom_attributes.clear()
                addressIn.customer.custom_attributes.addAll(userObj.custom_attributes!!)

                // Check if user adding new address or Editing/Updating an existing address
                if (model.isEditable) {
                    // Edit existing address
                    val addressIdOfEditingAddress = model.addressIdToEdit

                    val savedAddresses = addressIn.customer.addresses
                    for (address in savedAddresses) {
                        if (address.id.equals(addressIdOfEditingAddress)) {

                            // Update address fields
                            // Delete old steet and add new
                            address.street.clear()
                            address.street.add(street)
                            address.city = town
                            address.district = townId
                            address.postcode = pincode
                            address.region = Region(
                                region = regionName,
                                region_code = regionCode,
                                region_id = stateId.toInt()
                            )
                            address.region_id = stateId
                        }
                    }
                } else {
                    //  Customer new Address
                    val newAddress = Addresses(
                        city = town,
                        country_id = "IN",
                        customer_id = "$customerId",
                        firstname = MyApplication.sharedPref.getString(
                            PreferenceKeys.FIRST_NAME,
                            ""
                        )
                            ?: "",
                        id = "0",
                        lastname = MyApplication.sharedPref.getString(PreferenceKeys.LAST_NAME, "")
                            ?: "",
                        postcode = pincode,
                        district = townId,
                        region = region,
                        region_id = "$stateId",
                        street = arrayListOf(street),
                        telephone = MyApplication.sharedPref.getString(PreferenceKeys.PHONE_NO, "")
                            ?: ""
                    )
                    addressIn.customer.addresses.add(newAddress)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            addOrUpdateAddressData = addressRepo.addOrUpdateAddress(custId, addressIn, true)
            isLoading.postValue(true)
        }
    }

    fun deleteAddressByCommonModel(addressId: String) {
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
        addressIn.customer.store_id =
            UtilsFunctions.getLanguageStoreId(MyApplication.instance).toInt()
        addressIn.customer.website_id = AppConstants.WEBSITE_ID.toInt()
        addressIn.customer.disable_auto_group_change =
            AppConstants.DISABLE_AUTO_GROUP_CHANGE.toInt()

        // All custom attributes from Login Model
        val rawUserObj = MyApplication.sharedPref.getString(PreferenceKeys.USER_OBJECT, "")
        val myType = object : TypeToken<LoginOut>() {}.type
        val userObj = MyApplication.gson.fromJson<LoginOut>(rawUserObj, myType)

        addressIn.customer.custom_attributes.clear()
        addressIn.customer.custom_attributes.addAll(userObj.custom_attributes!!)

        // All customer addresses + New address (or Updated address)
        // Customer all saved addresses
        val savedAddress =
            MyApplication.sharedPref.getString(PreferenceKeys.USER_ALL_ADDRESS, "") ?: ""
        if (!savedAddress.isEmpty()) {
            val myType = object : TypeToken<ArrayList<Addresses>>() {}.type
            val allAddresses = MyApplication.gson.fromJson<ArrayList<Addresses>>(
                savedAddress,
                myType
            )

            for (address in allAddresses) {
                if (address.id.equals(addressId)) {
                    allAddresses.remove(address)
                    break
                }
            }
            addressIn.customer.addresses.addAll(allAddresses)

            // Same method for ADD/UPDATE/DELETE address
            deleteOrDefaultAddressData = addressRepo.addOrUpdateAddress(custId, addressIn, true)
            isLoading.postValue(true)
        }
    }

    fun setAnAddressAsDefaultByCommonModel(addressId: String) {
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
        addressIn.customer.store_id =
            UtilsFunctions.getLanguageStoreId(MyApplication.instance).toInt()
        addressIn.customer.website_id = AppConstants.WEBSITE_ID.toInt()
        addressIn.customer.disable_auto_group_change =
            AppConstants.DISABLE_AUTO_GROUP_CHANGE.toInt()

        // All custom attributes from Login Model
        val rawUserObj = MyApplication.sharedPref.getString(PreferenceKeys.USER_OBJECT, "")
        val myType = object : TypeToken<LoginOut>() {}.type
        val userObj = MyApplication.gson.fromJson<LoginOut>(rawUserObj, myType)

        addressIn.customer.custom_attributes.clear()
        addressIn.customer.custom_attributes.addAll(userObj.custom_attributes!!)

        // All customer addresses + New address (or Updated address)
        // Customer all saved addresses
        val savedAddress =
            MyApplication.sharedPref.getString(PreferenceKeys.USER_ALL_ADDRESS, "") ?: ""
        if (!savedAddress.isEmpty()) {
            val myType = object : TypeToken<ArrayList<Addresses>>() {}.type
            val allAddresses = MyApplication.gson.fromJson<ArrayList<Addresses>>(
                savedAddress,
                myType
            )

            for (address in allAddresses) {
                if (address.id.equals(addressId)) {
                    address.default_billing = true
                    address.default_shipping = true
                } else {
                    address.default_billing = false
                    address.default_shipping = false
                }
            }
            addressIn.customer.addresses.addAll(allAddresses)

            // Same method for ADD/UPDATE/DELETE address
            deleteOrDefaultAddressData = addressRepo.addOrUpdateAddress(custId, addressIn, true)
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

    fun deleteOrDefaultAnAddressResponse(): LiveData<AddressOut> {
        return deleteOrDefaultAddressData
    }

    fun deleteAddressResponse(): LiveData<Boolean> {
        return deleteAddressData
    }

    fun getAddressByIdResponse(): LiveData<Addresses> {
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