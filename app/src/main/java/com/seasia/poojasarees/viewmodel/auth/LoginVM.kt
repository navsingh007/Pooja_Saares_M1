package com.seasia.poojasarees.viewmodel.auth

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.authentication.LoginOut
import com.seasia.poojasarees.repository.auth.LoginRepo

class LoginVM : ViewModel() {
    private var loginRepo: LoginRepo
    private var loginData: MutableLiveData<LoginOut>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        loginRepo = LoginRepo()
        loginData = MutableLiveData<LoginOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        loginData = loginRepo.loginResponse(null, false)
    }

    fun onLogin(view: View, etMobile: EditText, etPassword: EditText) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val mobile = etMobile.text.toString()
            val password = etPassword.text.toString()

            if (mobile.isEmpty() && password.isEmpty()) {
                userMsg.postValue(ENTER_MOBILE_N_PASSWORD)
                return
            }

            if (password.isEmpty()) {
                userMsg.postValue(ENTER_PASSWORD)
                return
            }

            if (mobile.length < 10) {
                userMsg.postValue(MOBILE_LENGH)
            } else if (password.length < 6) {
                userMsg.postValue(PASSWORD_LENGH)
            } else if (!UtilsFunctions.isValidPassword(password)) {
                userMsg.postValue(PASSWORD_INVALID)
            } else {
                val loginIn = JsonObject()
                loginIn.addProperty("phone", mobile)
                loginIn.addProperty("password", password)
                loginData = loginRepo.loginResponse(loginIn, true)
                isLoading.postValue(true)
            }
        }
    }

    fun loginResponse(): LiveData<LoginOut> {
        return loginData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun isServerError(): LiveData<Boolean> {
        return loginRepo.isServerError()
    }

    fun showApiMsg(): LiveData<String> {
        return loginRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    companion object {
        val ENTER_MOBILE_N_PASSWORD = "enterMobileNPassword"
        val ENTER_PASSWORD = "enterPassword"
        val MOBILE_LENGH = "mobileLength"
        val PASSWORD_LENGH = "passLength"
        val PASSWORD_INVALID = "passInvalid"
    }
}