package com.seasia.poojasarees.viewmodel.profile

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.constants.AppConstants
import com.seasia.poojasarees.model.helper.Profile
import com.seasia.poojasarees.model.request.UpdateProfileIn
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.profile.ProfileOut
import com.seasia.poojasarees.repository.profile.ProfileRepo
import com.seasia.poojasarees.utils.PreferenceKeys

class ProfileVM : ViewModel() {
    private var profileRepo: ProfileRepo
    private var getProfileData: MutableLiveData<ProfileOut>
    private var updateProfileData: MutableLiveData<ProfileOut>
    private var allStatesData: MutableLiveData<ArrayList<AllStatesOut>>
    private var allTownsData: MutableLiveData<ArrayList<AllTownsOut>>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>
    val profile = Profile()

    init {
        profileRepo = ProfileRepo()
        getProfileData = MutableLiveData<ProfileOut>()
        updateProfileData = MutableLiveData<ProfileOut>()
        allStatesData = MutableLiveData<ArrayList<AllStatesOut>>()
        allTownsData = MutableLiveData<ArrayList<AllTownsOut>>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        isLoading.postValue(false)
        allStatesData = profileRepo.allStatesResponse(false)
//        getProfileData = profileRepo.getProfileResponse("", false)
        getProfileData = profileRepo.getProfileResponse(false)
        updateProfileData = profileRepo.updateProfileResponse("", null, false)
        allTownsData = profileRepo.getAllTowns(true)
    }

    fun getAllStates() {
        allStatesData = profileRepo.allStatesResponse(true)
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return profileRepo.isSessionExpire()
    }

    fun onUpdateClick(view: View, model: Profile) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val name = model.name
            val lastName = model.lastName
            val shopName = model.shopName
            val mobleNo = model.mobileNo
            var emailId = model.emailId
            val gstNo = model.gstNo
//            val street = model.street
            val town = model.town
//            val state = model.state
//            val pincode = model.pincode

            // Check if empty
            val userInputs = arrayOf(name, lastName, mobleNo)
            val msg = arrayOf(ENTER_NAME, ENTER_LAST_NAME, ENTER_MOBILE_NO)

/*            val msg = arrayOf(
                MyApplication.instance.resources.getString(R.string.register_warn_name),
                MyApplication.instance.resources.getString(R.string.register_warn_mobile_no),
                MyApplication.instance.resources.getString(R.string.register_warn_town)
            )*/

            for (i in 0..userInputs.size - 1) {
                if (TextUtils.isEmpty(userInputs[i])) {
//                    val title =
//                        MyApplication.instance.resources.getString(R.string.register_warn_title)
//                    val warning = "$title ${msg[i]}"
//                    UtilsFunctions.showToastWarning(warning)

                    userMsg.postValue(msg[i])
                    return
                }
            }

            if (name.length < 3) {
                userMsg.postValue(MIN_NAME)
                return
            }

            if (lastName.length < 3) {
                userMsg.postValue(MIN_LAST_NAME)
                return
            }
            if (mobleNo.length < 10) {
                userMsg.postValue(MIN_MOBILE_NO)
//                UtilsFunctions.showToastWarning(MyApplication.instance.resources.getString(R.string.register_warn_mobile))
                return
            }

            if (!gstNo.isEmpty() && gstNo.length < 10) {
                userMsg.postValue(MIN_GST)
//                UtilsFunctions.showToastWarning(MyApplication.instance.resources.getString(R.string.profile_valid_gst))
                return
            }

            if (!shopName.isEmpty() && shopName.length < 3) {
                userMsg.postValue(MIN_SHOP)
//                UtilsFunctions.showToastWarning(MyApplication.instance.resources.getString(R.string.register_warn_shopName))
                return
            }

            if (!emailId.isEmpty() && !UtilsFunctions.isValidEmail(emailId)) {
                userMsg.postValue(VALID_EMAIL)
//                UtilsFunctions.showToastWarning(MyApplication.instance.resources.getString(R.string.profile_valid_email))
                return
            }

            if (emailId.isEmpty()) {
                // If email ID empty, add a default email ID
                emailId = "${mobleNo}_DEFAULT_ID@gmail.com"
            }

            if (town.isEmpty()) {
                userMsg.postValue(SELECT_TOWN)
                return
            }

            Log.d("ProfileVM", "===============> $model")

            val custId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "")

            val updateProfile = UpdateProfileIn()
            updateProfile.customer.id = custId ?: ""
            updateProfile.customer.firstname = name
            updateProfile.customer.lastname = lastName
            updateProfile.customer.email = emailId
            updateProfile.customer.taxvat = gstNo

            var gender = 0
            if (!model.gender.isEmpty()) {
                gender = model.gender.toInt()
            }
            updateProfile.customer.gender = gender
            updateProfile.customer.dob = model.dob
            updateProfile.customer.group_id = AppConstants.GROUP_ID
            updateProfile.customer.website_id = AppConstants.WEBSITE_ID
            updateProfile.customer.disable_auto_group_change =
                AppConstants.DISABLE_AUTO_GROUP_CHANGE

            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "phone_number",
                    mobleNo
                )
            )
            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "shopname",
                    shopName
                )
            )
            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "town_city_dropdown",
                    town
                )
            )
//            updateProfile.customer.custom_attributes.add(
//                UpdateProfileIn.CustomAttribute(
//                    "gender",
//                    model.gender
//                )
//            )
            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "marital_status",
                    model.maritalStatus
                )
            )

            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "qualification",
                    model.education
                )
            )
            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "language_known",
                    model.languagesKnown
                )
            )
            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "hobbies",
                    model.hobbies
                )
            )
            updateProfile.customer.custom_attributes.add(
                UpdateProfileIn.CustomAttribute(
                    "is_approval",
                    "1"
                )
            )

            // Address part not required as of now

/*            val defaultAddressId =
                MyApplication.sharedPref.getString(PreferenceKeys.DEFAULT_ADDRESS_ID, "") ?: ""
            val defaultAddress =
                MyApplication.sharedPref.getString(PreferenceKeys.USER_DEFAULT_ADDRESS, "") ?: ""
            val customerId =
                MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""
            val firstName =
                MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME, "") ?: ""

            if (!defaultAddressId.isEmpty() && !customerId.isEmpty()) {
                // Update an existing address using old address ID
                try {
                    val updateAddress = UpdateProfileIn.Addresse(
                        city = town,
                        customer_id = customerId.toInt(),
                        country_id = "IN",
                        id = defaultAddressId.toInt(),
                        firstname = firstName,
                        lastname = firstName,
                        street = arrayListOf(street),
                        postcode = pincode,
                        telephone = MyApplication.sharedPref.getString(PreferenceKeys.PHONE_NO, "") ?: ""
                    )
                    updateProfile.customer.addresses.add(updateAddress)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                // Create new address for new id - 0
                try {
                    val updateAddress = UpdateProfileIn.Addresse(
                        city = town,
                        customer_id = customerId.toInt(),
                        country_id = "IN",
                        id = 0,
                        firstname = firstName,
                        lastname = firstName,
                        street = arrayListOf(street),
                        postcode = pincode,
                        telephone = MyApplication.sharedPref.getString(PreferenceKeys.PHONE_NO, "") ?: ""
                    )
                    updateProfile.customer.addresses.add(updateAddress)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }*/

            updateProfileData = profileRepo.updateProfileResponse(custId!!, updateProfile, true)
            isLoading.postValue(true)
        }
    }

    fun updateProfileResponse(): LiveData<ProfileOut> {
        return updateProfileData
    }

    fun onGetProfile() {
        val userId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "")
//        getProfileData = profileRepo.getProfileResponse(userId!!, true)
        getProfileData = profileRepo.getProfileResponse(true)
        isLoading.postValue(true)
    }

    fun getProfileResponse(): LiveData<ProfileOut> {
        return getProfileData
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
        return profileRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    companion object {
        val ENTER_NAME = "enterName"
        val ENTER_LAST_NAME = "enterLastName"
        val ENTER_TOWN = "enterTown"
        val ENTER_MOBILE_NO = "enterMobile"
        val ENTER_EMAIL = "enterEmail"

        val MIN_NAME = "minName"
        val MIN_LAST_NAME = "minLastName"
        val MIN_GST = "minGST"
        val MIN_SHOP = "minShop"
        val MIN_MOBILE_NO = "minMobile"
        val VALID_EMAIL = "valideMail"

        val SELECT_TOWN = "selectTown"

        var emailId = ""
    }
}