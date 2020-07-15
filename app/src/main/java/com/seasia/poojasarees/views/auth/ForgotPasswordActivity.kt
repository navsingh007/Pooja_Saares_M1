package com.seasia.poojasarees.views.auth

import android.content.Intent
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityForgotPasswordBinding
import com.seasia.poojasarees.viewmodel.auth.ForgotPasswordVM

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var forgotPassVM: ForgotPasswordVM

    override fun getLayoutId(): Int {
        return R.layout.activity_forgot_password
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityForgotPasswordBinding

        forgotPassVM = ViewModelProvider(this).get(ForgotPasswordVM::class.java)

//        forgotPassVM = ForgotPasswordVM(this)

        binding.btnSubmit.setOnClickListener {

            UtilsFunctions.hideKeyBoard(binding.btnSubmit)
            checkMobileAndSubmit()
        }
        setToolbar()
        isAlreadyRegisteredPhoneObserver()
        loadingObserver()
        showApiMsgObserver()
    }

    private fun setToolbar() {
        binding.rlToolbar.tvCommonHeading.setText(resources.getString(R.string.forgot_password))
    }

    private fun checkMobileAndSubmit() {
        val mobile = binding.etMobileNo.text.toString()
        if (mobile.length < 10) {
            UtilsFunctions.showToastWarning(resources.getString(R.string.forgot_invalid_mobile))
        } else {

            // Validate if phone no. is registered or not
            forgotPassVM.phoneNoCheck(mobile)
        }
    }

    private fun isAlreadyRegisteredPhoneObserver() {
        forgotPassVM.isValidPhone().observe(this, Observer { isAlreadyRegisteredPhone ->
            stopProgressDialog()

            if (isAlreadyRegisteredPhone != null) {
                val msg = isAlreadyRegisteredPhone.message ?: ""
                if (msg.equals("Phone number not registered.")) {
                    UtilsFunctions.showToastWarning(resources.getString(R.string.login_phone_not_registered))
                } else {
                    UtilsFunctions.showToastWarning(isAlreadyRegisteredPhone.message ?: "")
                }
            } else {
                val intent = Intent(this, OtpVerificationActivity::class.java)
                intent.putExtra("mobile", binding.etMobileNo.text.toString())
                startActivity(intent)
                finish()
            }
        })
    }

    private fun loadingObserver() {
        forgotPassVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        forgotPassVM.showApiMsg().observe(this, Observer { msg ->
            if (msg != null) {
                if (msg.equals("User with this phone already exist.")) {
                    UtilsFunctions.showToastError(
                        resources.getString(
                            R.string.register_phone_already_exist
                        )
                    )
                } else {
                    UtilsFunctions.showToastError(msg)
                }
            }
        })
    }
}