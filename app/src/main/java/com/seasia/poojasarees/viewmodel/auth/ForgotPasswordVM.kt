package com.seasia.poojasarees.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.authentication.SignupPhoneNoOut
import com.seasia.poojasarees.repository.auth.ForgotPasswordRepo

class ForgotPasswordVM : ViewModel() {
    private var forgotPassRepo: ForgotPasswordRepo
    private var phoneValidData: MutableLiveData<SignupPhoneNoOut>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        forgotPassRepo = ForgotPasswordRepo()
        phoneValidData = MutableLiveData<SignupPhoneNoOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        phoneValidData = forgotPassRepo.phoneNoCheck(null, false)
    }

    fun phoneNoCheck(phontNo: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            val isValidPhone = JsonObject()
            isValidPhone.addProperty("phone", phontNo)
            phoneValidData = forgotPassRepo.phoneNoCheck(isValidPhone, true)
            isLoading.postValue(true)
        }
    }

    fun isValidPhone(): LiveData<SignupPhoneNoOut> {
        return phoneValidData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return forgotPassRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }
}