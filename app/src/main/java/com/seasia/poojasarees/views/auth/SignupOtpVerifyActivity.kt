package com.seasia.poojasarees.views.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityOtpVerificationBinding
import com.seasia.poojasarees.model.request.SignUpIn
import com.seasia.poojasarees.model.response.authentication.SignUpOut
import com.seasia.poojasarees.utils.DialogClass
import com.seasia.poojasarees.viewmodel.auth.OtpVM
import com.seasia.poojasarees.viewmodel.auth.SignupVM

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class SignupOtpVerifyActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpVerificationBinding
    private lateinit var otpVM: OtpVM
    private lateinit var signupVM: SignupVM
    private lateinit var mobileNo: String
    private var signupCust: SignUpIn? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_otp_verification
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityOtpVerificationBinding

        otpVM = ViewModelProvider(this).get(OtpVM::class.java)
//        otpVM = OtpVM(this)
        binding.otpVerify = otpVM

        signupVM = ViewModelProvider(this).get(SignupVM::class.java)
//        signupVM = SignupVM(this)

        sendOtpResponseObserver()
        verifyOtpResponseObserver()
        signUpResponseObserver()

        getExtras()
        otpVM.sendOtpAtMobile(mobileNo)

        setToolbar()
        getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
        loadingObserver()
        signupLoadingObserver()

        singupChanges()
        showApiMsgObserver()
        showUserWarningObserver()
        moveViewToCenterOnKeyboardOpen()
    }

    private fun singupChanges() {
        binding.tvForgotPasswordHeading.setText(resources.getString(R.string.register_heading))
        binding.rlToolbar.tvCommonHeading.setText(resources.getString(R.string.register_heading))
    }

    fun resendOtp(v: View) {
        if (UtilsFunctions.isNetworkConnected()) {
            otpVM.sendOtpAtMobile(mobileNo)
        }
    }

    private fun setToolbar() {
        binding.rlToolbar.tvCommonHeading.setText(resources.getString(R.string.verification_code))
    }

    private fun getExtras() {
        mobileNo = intent.getStringExtra("mobile")

        // Bold mobile no. in description
        val description = binding.tvOtpSent.text.toString()
        val otpAtMobile = SpannableStringBuilder()
            .append(description)
            .append(" xxxxxxx${mobileNo.substring(7)}")
//            .bold { append(" $mobileNo") }

        binding.tvOtpSent.text = otpAtMobile

        // Customer Object
        signupCust = intent.getSerializableExtra("customerObj") as SignUpIn
    }

    private fun sendOtpResponseObserver() {
        otpVM.sendOtpResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
//                UtilsFunctions.showToastSuccess(it.message!!)
                UtilsFunctions.showToastSuccess(MyApplication.instance.resources.getString(R.string.otp_sent))
            }
        })
    }

    private fun verifyOtpResponseObserver() {
        otpVM.verifyOtpResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
                UtilsFunctions.showToastSuccess(MyApplication.instance.resources.getString(R.string.otp_verified))

                Log.d("SignupOtp", "======> CREATE CUSTOMER NOW ${signupCust.toString()} ")

                if (signupCust != null) {
                    signupVM.signupAfterOtpVerify(signupCust!!)
                } else {
                    UtilsFunctions.showToastError(resources.getString(R.string.something_error))
                }
            }
        })
    }

    private fun signUpResponseObserver() {
        signupVM.signupResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra("redirectedFromSignup", true)
                )
            }

            // Show success
//            showSuccessDialog(it)
        })
    }

    private fun showSuccessDialog(it: SignUpOut?) {
        if (it != null) {
//                finish()
//                UtilsFunctions.showToastSuccess("Signed up successfully $it")

            val dialog = DialogClass().setConfirmationDialog(
                this,
                "SignedUp",
                R.layout.dialog_thanks_for_signup
            )

            val btnOk = dialog.findViewById<Button>(R.id.btnOK)
            btnOk.setOnClickListener {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    .putExtra("message", it.message)
                )
                finish()
            }
            dialog.show()
        }
    }

    private fun loadingObserver() {
        otpVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun signupLoadingObserver() {
        signupVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        otpVM.showApiMsg().observe(this, Observer { msg ->
            stopProgressDialog()

            if (msg != null) {
                if (msg.equals("Invalid OTP")) {
                    UtilsFunctions.showToastError(
                        resources.getString(
                            R.string.otp_invalid
                        )
                    )
                } else if (msg.equals("INVALID MOBILE NUMBER")) {
                    UtilsFunctions.showToastError(
                        resources.getString(
                            R.string.otp_invalid_mobile_number
                        )
                    )
                    finish()
                } else if (msg.equals("OTP sent successfully")) {
                    UtilsFunctions.showToastError(
                        resources.getString(
                            R.string.otp_sent
                        )
                    )
                } else if (msg.equals("OTP verified successfully")) {
                    UtilsFunctions.showToastError(
                        resources.getString(
                            R.string.otp_verified
                        )
                    )
                } else {
                    UtilsFunctions.showToastError(msg)
                }
            }
        })
    }

    private fun showUserWarningObserver() {
        otpVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                when (msg) {
                    OtpVM.MIN_OTP -> {
                        UtilsFunctions.showToastWarning(resources.getString(R.string.otp_hint))
                    }
                    OtpVM.ENTER_OTP -> {
                        UtilsFunctions.showToastWarning(resources.getString(R.string.otp_hint_warn))
                    }
                }
            }
        })
    }

    // Auto OTP
    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("otp"))
        super.onResume()

        if (checkAndRequestPermissionSms()){}
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action.equals("otp", ignoreCase = true)) {
                val message = intent.getStringExtra("message")

                // Extract number only OTP from message String
                if (message != null) {
                    val numberOnly = message.replace("[^0-9]".toRegex(), "")
                    binding.pinview.setOTP(numberOnly)
                }
            }
        }
    }

    private fun moveViewToCenterOnKeyboardOpen() {
        KeyboardVisibilityEvent.setEventListener(
            this,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    // write your code
                    if (isOpen) {
                        binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_DOWN) })
                    } else {
                        binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_UP) })
                    }
                }
            })
    }
}