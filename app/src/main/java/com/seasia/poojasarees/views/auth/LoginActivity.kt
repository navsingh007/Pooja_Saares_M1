package com.seasia.poojasarees.views.auth

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityLoginBinding
import com.seasia.poojasarees.fcm.FcmUtils
import com.seasia.poojasarees.utils.DialogClass
import com.seasia.poojasarees.utils.LocaleManager
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.auth.LoginVM
import com.seasia.poojasarees.views.home.HomeActivity
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginVM: LoginVM
//    private lateinit var adminVM: AdminVM

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityLoginBinding
        loginVM = ViewModelProvider(this).get(LoginVM::class.java)

//        val intanceId = FcmUtils.getInstanceId()
        val instanceId = MyApplication.sharedPref.getString(PreferenceKeys.FCM_DEVICE_TOKEN, "") ?: ""
        Log.d("Login", "ID - $instanceId")

//        loginVM = LoginVM(this)
        binding.loginVM = loginVM

//        adminVM = ViewModelProvider(this).get(AdminVM::class.java)
//        adminTokenObserver()

        clickListeners()
        loginResponseObserver()
        loadingObserver()
        serverErrorObserver()
        showApiMsgObserver()
        showUserWarningObserver()
        getExtras()

//        focusOnView()

        moveViewToCenterOnKeyboardOpen()
    }

    private fun getExtras() {
        val isRedirected = intent.getBooleanExtra("redirectedFromSignup", false)
        if (isRedirected) {

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

    private fun clickListeners() {
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        when (LocaleManager.getLanguagePref(this)) {
            LocaleManager.HINDI -> {
                val viewInHindi = "View in English"
                binding.tvViewInHindi.setText(viewInHindi)
            }
            LocaleManager.ENGLISH -> {

                val viewInEng = "हिंदी में देखें"
                binding.tvViewInHindi.setText(viewInEng)
            }
        }

        binding.tvViewInHindi.setOnClickListener {
            when (LocaleManager.getLanguagePref(this)) {
                LocaleManager.HINDI -> {
                    val viewInEng = "हिंदी में देखें"
                    binding.tvViewInHindi.setText(viewInEng)
                    LocaleManager.setNewLocale(this, LocaleManager.ENGLISH)
                }
                LocaleManager.ENGLISH -> {
                    val viewInHindi = "View in English"
                    binding.tvViewInHindi.setText(viewInHindi)
                    LocaleManager.setNewLocale(this, LocaleManager.HINDI)
                }
            }

            val intent: Intent = intent
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    private fun loginResponseObserver() {
        loginVM.loginResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
                MyApplication.sharedPref.save(PreferenceKeys.USER_OBJECT, it)

                MyApplication.sharedPref.saveString(PreferenceKeys.FIRST_NAME, it.firstname ?: "")
                MyApplication.sharedPref.saveString(PreferenceKeys.LAST_NAME, it.lastname ?: "")
                MyApplication.sharedPref.saveString(
                    PreferenceKeys.CUSTOMER_TOKEN,
                    it.customerToken ?: ""
                )
                // Cust ID
                MyApplication.sharedPref.saveString(PreferenceKeys.CUSTOMER_ID, it.id.toString())

                // Email ID
                MyApplication.sharedPref.saveString(PreferenceKeys.EMAIL, it.email ?: "")

                // Login session
                MyApplication.sharedPref.saveBoolean(PreferenceKeys.IS_LOGIN, true)

                // Gender
                MyApplication.sharedPref.saveString(
                    PreferenceKeys.GENDER,
                    it.gender?.toString() ?: ""
                )

                // Default Address
                val userAddress = it.addresses
                if (userAddress != null) {
                    // SAVE all User addresses

                    MyApplication.sharedPref.save(PreferenceKeys.USER_ALL_ADDRESS, userAddress)

                    for (address in userAddress) {
                        if (address.default_billing != null && address.default_billing) {
                            // Create complete default user address
                            val street = address.street?.get(0) ?: ""
                            val town = address.city ?: ""
                            val state = address.region?.region ?: ""
                            val postcode = address.postcode ?: ""

                            val completeAddress = "${street}, ${town}, ${state}, ${postcode}"
                            MyApplication.sharedPref.saveString(
                                PreferenceKeys.USER_DEFAULT_ADDRESS,
                                completeAddress
                            )

                            // Set default address ID
                            MyApplication.sharedPref.saveString(
                                PreferenceKeys.DEFAULT_ADDRESS_ID,
                                address.id ?: ""
                            )
                            break
                        }
                    }
                }

                // User Profile Pic and Phone no.
                var profilePic = ""
                if (it.custom_attributes != null) {
                    for (i in 0..it.custom_attributes.size - 1) {
                        val attributeCode = it.custom_attributes[i].attribute_code
                        val value = it.custom_attributes[i].value

                        if (attributeCode.equals("profile_pic")) {
                            profilePic = value ?: ""
                        }
                        if (attributeCode.equals("phone_number")) {
                            MyApplication.sharedPref.saveString(
                                PreferenceKeys.PHONE_NO,
                                value ?: ""
                            )
                        }
                        if (attributeCode.equals("shopname")) {
                            MyApplication.sharedPref.saveString(
                                PreferenceKeys.SHOP_NAME,
                                value ?: ""
                            )
                        }
                    }
                }
                MyApplication.sharedPref.saveString(
                    PreferenceKeys.PROFILE_PIC,
                    profilePic
                )

                // CMS pages
                MyApplication.sharedPref.saveString(
                    PreferenceKeys.ABOUT_US,
                    it.cmsLinks?.about_us ?: ""
                )
                MyApplication.sharedPref.saveString(
                    PreferenceKeys.TERMS_N_CONDITIONS,
                    it.cmsLinks?.terms_conditions ?: ""
                )
                MyApplication.sharedPref.saveString(
                    PreferenceKeys.PRIVACY_POLICY,
                    it.cmsLinks?.privacy_policy ?: ""
                )

                UtilsFunctions.showToastSuccess(resources.getString(R.string.login_success))
                startActivity(Intent(this, HomeActivity::class.java))
            }
        })
    }

    private fun loadingObserver() {
        loginVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun serverErrorObserver() {
        loginVM.isServerError().observe(this, Observer {
            if (it) {
                UtilsFunctions.serverErrorDialog(this)
            }
        })
    }

    private fun showApiMsgObserver() {
        loginVM.showApiMsg().observe(this, Observer { msg ->
            stopProgressDialog()

            if (msg != null) {
                if (msg.equals("Phone number is not registered")) {
                    UtilsFunctions.showToastError(
                        resources.getString(
                            R.string.login_phone_not_registered
                        )
                    )
                } else if (msg.equals("Invalid phone or password.")) {
                    UtilsFunctions.showToastError(resources.getString(R.string.login_invalid_mobile_or_pass))
                } else if (msg.equals("Your account is not approved by Admin.")) {
                    UtilsFunctions.showToastError(resources.getString(R.string.login_acc_not_approved))
                } else {
                    UtilsFunctions.showToastError(msg)
                }
            }
        })
    }

    private fun showUserWarningObserver() {
        loginVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                if (msg.equals(LoginVM.MOBILE_LENGH)) {
                    UtilsFunctions.showToastWarning(resources.getString(R.string.login_valid_mobile))
                } else if (msg.equals(LoginVM.ENTER_PASSWORD)) {
                    UtilsFunctions.showToastWarning(resources.getString(R.string.login_pass_hint_warn))
                } else if (msg.equals(LoginVM.PASSWORD_LENGH)) {
                    UtilsFunctions.showToastWarning(resources.getString(R.string.login_pass_hint))
                } else if (msg.equals(LoginVM.PASSWORD_INVALID)) {
                    UtilsFunctions.showToastWarning(resources.getString(R.string.register_warn_password_chars))
                } else if (msg.equals(LoginVM.ENTER_MOBILE_N_PASSWORD)) {
                    UtilsFunctions.showToastWarning(resources.getString(R.string.login_mobile_n_password))
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()

        binding.etMobileNo.setText("")
        binding.etPassword.setText("")
//        binding.etMobileNo.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        UtilsFunctions.hideKeyboardActivity(this)

        binding.rlRoot.isFocusable = true
        binding.rlRoot.isFocusableInTouchMode = true
    }

    private fun moveViewToCenterOnKeyboardOpen() {
        KeyboardVisibilityEvent.setEventListener(
            this,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    // write your code
//                    if (isOpen) {
//                        binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_DOWN) })
//                    } else {
//                        binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_UP) })
//                    }
                }
            })


//        binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_DOWN) })

/*        binding.etMobileNo.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                binding.svRoot.smoothScrollTo(100, binding.etMobileNo.bottom)
                binding.svRoot.post(Runnable { binding.etMobileNo.requestFocus() }
                )
                return false
            }
        })

        binding.etPassword.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                binding.svRoot.smoothScrollTo(200, binding.etPassword.bottom)


                binding.svRoot.post(Runnable { binding.etPassword.requestFocus() }
                )
                return false
            }
        })*/
    }

    private fun focusOnView() {
        binding.svRoot.post(Runnable { binding.svRoot.scrollTo(0, binding.etMobileNo.top) })
        binding.svRoot.post(Runnable { binding.svRoot.scrollTo(0, binding.etPassword.top) })
    }
}