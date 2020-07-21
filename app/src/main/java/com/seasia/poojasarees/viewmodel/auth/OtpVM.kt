package com.seasia.poojasarees.viewmodel.auth

import `in`.aabhasjindal.otptextview.OtpTextView
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import com.goodiebag.pinview.Pinview
import com.google.gson.JsonObject
//import com.mukesh.OtpView
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.authentication.OtpOut
import com.seasia.poojasarees.repository.auth.OtpRepo

class OtpVM : ViewModel() {
    private var sendOtpData: MutableLiveData<OtpOut>
    private var verifyOtpData: MutableLiveData<OtpOut>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>
    private var otpRepo: OtpRepo
    private var mobileNo: String = ""

    init {
        otpRepo = OtpRepo()
        sendOtpData = MutableLiveData<OtpOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        sendOtpData = otpRepo.sendOtpResponse(null, false)
        verifyOtpData = otpRepo.verifyOtpResponse(null, false)
    }

//    fun onOtpSubmit(view: View, pin: Pinview) {
//    fun onOtpSubmit(view: View, pin: OtpView) {
    fun onOtpSubmit(view: View, pin: OtpTextView) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val otpNo = pin.otp ?: ""

            if (otpNo.length == 0) {
                userMsg.postValue(ENTER_OTP)
                return
            }

            if (otpNo.length < 6) {
                userMsg.postValue(MIN_OTP)
//                UtilsFunctions.showToastWarning(context.resources.getString(R.string.otp_hint))
                return
            }

            val otpIn = JsonObject()
            otpIn.addProperty("phone", mobileNo)
            otpIn.addProperty("otp", otpNo)
            verifyOtpData = otpRepo.verifyOtpResponse(otpIn, true)
            isLoading.postValue(true)
        }
    }

    fun sendOtpAtMobile(mobile: String) {
        mobileNo = mobile

        val otpIn = JsonObject()
        otpIn.addProperty("phone", mobile)
        sendOtpData = otpRepo.sendOtpResponse(otpIn, true)
        isLoading.postValue(true)
    }

    fun sendOtpResponse(): LiveData<OtpOut> {
        return sendOtpData
    }

    fun verifyOtpResponse(): LiveData<OtpOut> {
        return verifyOtpData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return otpRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    companion object {
        val ENTER_OTP = "enterOtp"
        val MIN_OTP = "minOtp"
    }
}