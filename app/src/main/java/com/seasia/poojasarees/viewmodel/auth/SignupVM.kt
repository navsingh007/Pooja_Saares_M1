package com.seasia.poojasarees.viewmodel.auth

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.helper.Signup
import com.seasia.poojasarees.model.request.SignUpIn
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.SignUpOut
import com.seasia.poojasarees.model.response.SignupPhoneNoOut
import com.seasia.poojasarees.repository.auth.SignupRepo
import com.seasia.poojasarees.utils.LocaleManager
import com.seasia.poojasarees.utils.PreferenceKeys
import kotlin.math.sign

class SignupVM : ViewModel() {
    private var signupRepo: SignupRepo
    private var signupData: MutableLiveData<SignUpOut>
    private var onSignupClick: MutableLiveData<SignUpIn>
    private var phoneValidData: MutableLiveData<SignupPhoneNoOut>
    private var allTownsData: MutableLiveData<ArrayList<AllTownsOut>>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        signupRepo = SignupRepo()
        signupData = MutableLiveData<SignUpOut>()
        onSignupClick = MutableLiveData<SignUpIn>()
        allTownsData = MutableLiveData<ArrayList<AllTownsOut>>()
        phoneValidData = MutableLiveData<SignupPhoneNoOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        signupData = signupRepo.signupResponse(null, false)
        phoneValidData = signupRepo.phoneNoCheck(null, false)
        allTownsData = signupRepo.getAllTowns(true)
    }

    fun onSubmitClick(view: View, model: Signup) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val name = model.name
            val lastName = model.lastName
            val shopName = model.shopName
            val town = model.town
            val mobleNo = model.mobileNo
            val password = model.password

            val userInputs = arrayOf(name, lastName, mobleNo, password)
            val msg = arrayOf(
                ENTER_NAME, ENTER_LAST_NAME, ENTER_MOBILE_NO, ENTER_PASSWORD
            )

            val userInputsWarn = arrayOf(name, lastName, mobleNo)
            val warnMsg = arrayOf(
                WARN_NAME, WARN_LAST_NAME, WARN_MOBILE_NO
            )

//            val msg = arrayOf(
//                context.resources.getString(R.string.register_warn_name),
//                context.resources.getString(R.string.register_warn_town),
//                context.resources.getString(R.string.register_warn_mobile_no),
//                context.resources.getString(R.string.register_warn_password)
//            )

            for (i in 0..userInputs.size - 1) {
                if (TextUtils.isEmpty(userInputs[i])) {
//                    val title =
//                        context.resources.getString(R.string.register_warn_title)
//                    val warning = "$title ${msg[i]}"
//                    UtilsFunctions.showToastWarning(warning)

                    userMsg.postValue(msg[i])
                    return
                }
            }

            for (i in 0..userInputsWarn.size - 1) {
                if (userInputsWarn[i].length < 3) {
//                    val title =
//                        context.resources.getString(R.string.register_warn_valid_title)
//                    val warning = "$title ${msg[i]}"
//                    UtilsFunctions.showToastWarning(warning)

                    userMsg.postValue(warnMsg[i])
                    return
                }
            }

            if (town.isEmpty()) {
                userMsg.postValue(SELECT_TOWN)
                return
            }

            if (password.length < 6) {
                userMsg.postValue(MIN_PASSWORD)
//                UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_password_hint))
                return
            }

            if (mobleNo.length < 10) {
                userMsg.postValue(MIN_MOBILE)
//                UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_mobile))
                return
            }

            if (!shopName.isEmpty() && shopName.length < 3) {
                userMsg.postValue(MIN_SHOP_NAME)
//                UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_shopName))
                return
            }

            if (!UtilsFunctions.isValidPassword(password)) {
                userMsg.postValue(INVALID_PASSWORD)
//                UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_password_chars))
                return
            }

            // Town drop down
            val signup = SignUpIn()
            signup.customer.email = "${mobleNo}_DEFAULT_ID@gmail.com"
            signup.customer.firstname = name
            signup.customer.lastname = lastName
            signup.customer.website_id = "1"
            signup.customer.group_id = "3"

            var localeId = ""
            when (LocaleManager.getLanguagePref(MyApplication.instance)) {
                LocaleManager.HINDI -> {
                    localeId = "2"
                }
                LocaleManager.ENGLISH -> {
                    localeId = "1"
                }
            }
            signup.customer.store_id = localeId

            signup.customer.custom_attributes.add(SignUpIn.CustomAttribute("phone_number", mobleNo))
            signup.customer.custom_attributes.add(SignUpIn.CustomAttribute("shopname", shopName))
//            signup.customer.custom_attributes.add(SignUpIn.CustomAttribute("town_city", town))
            signup.customer.custom_attributes.add(SignUpIn.CustomAttribute("town_city_dropdown", town))
            signup.customer.custom_attributes.add(SignUpIn.CustomAttribute("attribute_code", "0"))
            signup.password = password

            onSignupClick.postValue(signup)

//            isLoading.postValue(true)
//            signupData = signupRepo.signupResponse(signup, true)
        }
    }

    fun allTowns() : LiveData<ArrayList<AllTownsOut>> {
        return allTownsData
    }

    fun signupAfterOtpVerify(signUpIn: SignUpIn) {
        if (UtilsFunctions.isNetworkConnected()) {
            isLoading.postValue(true)
            signupData = signupRepo.signupResponse(signUpIn, true)
        }
    }

    fun onSignupClick(): LiveData<SignUpIn> {
        return onSignupClick
    }

    fun phoneNoCheck(phontNo: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            val isValidPhone = JsonObject()
            isValidPhone.addProperty("phone", phontNo)
            phoneValidData = signupRepo.phoneNoCheck(isValidPhone, true)
            isLoading.postValue(true)
        }
    }

    fun isValidPhone(): LiveData<SignupPhoneNoOut> {
        return phoneValidData
    }

    fun signupResponse(): LiveData<SignUpOut> {
        return signupData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return signupRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    companion object {
        val ENTER_NAME = "enterName"
        val ENTER_LAST_NAME = "enterLastName"
        val ENTER_TOWN = "enterTown"
        val ENTER_MOBILE_NO = "enterMobile"
        val ENTER_PASSWORD = "enterPassword"

        val WARN_NAME = "warnName"
        val WARN_LAST_NAME = "warnLastName"
        val WARN_TOWN = "warnTown"
        val WARN_MOBILE_NO = "warnMobile"

        val MIN_PASSWORD = "minPassword"
        val MIN_MOBILE = "minMobile"
        val MIN_SHOP_NAME = "minShopName"
        val INVALID_PASSWORD = "invalidPassword"

        val SELECT_TOWN = "selectTown"
    }
}