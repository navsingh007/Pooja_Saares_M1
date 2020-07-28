package com.seasia.poojasarees.views.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.SpannableStringBuilder
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityOtpVerificationBinding
import com.seasia.poojasarees.viewmodel.auth.OtpVM
import com.seasia.poojasarees.viewmodel.auth.OtpVM.Companion.ENTER_OTP
import com.seasia.poojasarees.viewmodel.auth.OtpVM.Companion.MIN_OTP
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class OtpVerificationActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpVerificationBinding
    private lateinit var otpVM: OtpVM
    private lateinit var mobileNo: String

    override fun getLayoutId(): Int {
        return R.layout.activity_otp_verification
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityOtpVerificationBinding

        otpVM = ViewModelProvider(this).get(OtpVM::class.java)
//        otpVM = OtpVM(this)
        binding.otpVerify = otpVM

        sendOtpResponseObserver()
        verifyOtpResponseObserver()

        getExtras()
        otpVM.sendOtpAtMobile(mobileNo)


        // Temporary - remove below code and uncomment above methods
//        binding.btnSubmit.setOnClickListener {
//            startActivity(
//                Intent(this, ChangePasswordActivity::class.java).putExtra(
//                    "mobile",
//                    mobileNo
//                )
//            )
//        }

        setToolbar()

        getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()

/*        pinviewTextwatcher()

        binding.btnSubmit.setOnClickListener {
            val a = binding.et1.text.toString()
            val b = binding.et2.text.toString()
            val c = binding.et3.text.toString()
            val d = binding.et4.text.toString()

            val otp = "$a$b$c$d"
            UtilsFunctions.showToastSuccess(otp)
        }*/

        moveViewToCenterOnKeyboardOpen()
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
//            .bold { append(" xxxxxx${mobileNo.substring(5)}") }

        binding.tvOtpSent.text = otpAtMobile
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
                startActivity(
                    Intent(this, ChangePasswordActivity::class.java).putExtra(
                        "mobile",
                        mobileNo
                    )
                )
                finish()
            }
        })
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

    private fun showApiMsgObserver() {
        otpVM.showApiMsg().observe(this, Observer { msg ->
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
                    MIN_OTP -> {
                        UtilsFunctions.showToastWarning(resources.getString(R.string.otp_hint))
                    }
                    ENTER_OTP -> {
                        UtilsFunctions.showToastWarning(resources.getString(R.string.otp_hint_warn))
                    }
                }
            }
        })
    }

/*    private fun pinviewTextwatcher() {
        binding.et1.addTextChangedListener(GenericTextWatcher(binding.et1, binding))
    }

    private class GenericTextWatcher constructor(private val view: View, val binding: ActivityOtpVerificationBinding) :
        TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (view.id) {
                R.id.et1 -> {
                    if (text.length == 1)
                        binding.et2.requestFocus()
                }
                R.id.et2 -> {
                    if (text.length == 1)
                        binding.et3.requestFocus()
                    else if (text.length == 0)
                        binding.et1.requestFocus()
                }
                R.id.et3 -> {
                    if (text.length == 1)
                        binding.et4.requestFocus()
                    else if (text.length == 0)
                        binding.et2.requestFocus()
                }
                R.id.et4 -> {
                    if(text.length==0)
                        binding.et3.requestFocus()
                }
                R.id.et5 -> {

                }
                R.id.et6 -> {

                }
            }
        }
    }*/


/*    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val text = editable.toString()

            binding.et1.id
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }*/

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