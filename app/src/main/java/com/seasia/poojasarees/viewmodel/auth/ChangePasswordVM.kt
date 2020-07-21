package com.seasia.poojasarees.viewmodel.auth

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.authentication.OtpOut
import com.seasia.poojasarees.repository.auth.ChangePasswordRepo

class ChangePasswordVM : ViewModel() {
    private var changePassRepo: ChangePasswordRepo
    private var changePassData: MutableLiveData<OtpOut>
    private val isLoading: MutableLiveData<Boolean>
    private var phone = ""
    private val userMsg: MutableLiveData<String>

    init {
        changePassRepo = ChangePasswordRepo()
        changePassData = MutableLiveData<OtpOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        changePassData = changePassRepo.changePassResponse(null, false)
    }

    fun setPhone(phoneNo: String) {
        phone = phoneNo
    }

    fun onSubmit(view: View, etNewPass: EditText, etConfirmPass: EditText) {
        if (UtilsFunctions.isNetworkConnected()) {
            UtilsFunctions.hideKeyBoard(view as Button)

            val newPass = etNewPass.text.toString()
            val confirmPass = etConfirmPass.text.toString()

            if (newPass.isEmpty()) {
                userMsg.postValue(ENTER_NEW_PASS)
                return
            }

            if (confirmPass.isEmpty()) {
                userMsg.postValue(ENTER_CONFIRM_PASS)
                return
            }

            if (newPass.length < 6 || confirmPass.length < 6) {
//                val passHint =
//                    context.resources.getString(R.string.change_password_hint)
//                UtilsFunctions.showToastWarning(passHint)

                userMsg.postValue(MIN_PASS)
                return
            }

            if (!UtilsFunctions.isValidPassword(newPass)) {
//                UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_password_chars))
                userMsg.postValue(INVALID_NEW_PASS)
                etNewPass.requestFocus()
                return
            }

            if (!UtilsFunctions.isValidPassword(confirmPass)) {
//                UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_password_chars))
                userMsg.postValue(INVALID_CONFIRM_PASS)
                etConfirmPass.requestFocus()
                return
            }

            if (!newPass.equals(confirmPass)) {
//                val notSamePass =
//                    context.resources.getString(R.string.change_password_not_match)
//                UtilsFunctions.showToastWarning(notSamePass)
                userMsg.postValue(PASS_NOT_MATCH)
                etNewPass.setText("")
                etConfirmPass.setText("")
                etNewPass.requestFocus()
            } else {
                val loginIn = JsonObject()
                loginIn.addProperty("phone", phone)
                loginIn.addProperty("password", newPass)
                changePassData = changePassRepo.changePassResponse(loginIn, true)
                isLoading.postValue(true)
            }
        }
    }

    fun changePassResponse(): LiveData<OtpOut> {
        return changePassData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    companion object {
        val ENTER_NEW_PASS = "enterNewPass"
        val ENTER_CONFIRM_PASS = "enterConfirmPass"
        val INVALID_NEW_PASS = "newPass"
        val INVALID_CONFIRM_PASS = "confirmPass"
        val MIN_PASS = "minPass"
        val PASS_NOT_MATCH = "passNotMatch"
    }
}