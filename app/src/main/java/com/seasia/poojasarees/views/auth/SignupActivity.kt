package com.seasia.poojasarees.views.auth

import android.content.Intent
import android.location.Location
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.FusedLocationClass
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivitySignupBinding
import com.seasia.poojasarees.helper.SignupHelper
import com.seasia.poojasarees.model.helper.Signup
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.utils.DialogClass
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.auth.SignupVM
import com.tiper.MaterialSpinner


class SignupActivity : BaseActivity(), FusedLocationClass.FusedLocationInterface {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupVM: SignupVM
    private var townAdapter: ArrayAdapter<String>? = null
    private var townList: ArrayList<String> = ArrayList()
    private var allTownsWithIdList: ArrayList<AllTownsOut> = ArrayList()
    private var signup: Signup? = null

    // Auto city enter
    private var fusedLocationClass: FusedLocationClass? = null
    private var city = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_signup
    }

    override fun initViews() {
        binding = viewDataBinding as ActivitySignupBinding

        signupVM = ViewModelProvider(this).get(SignupVM::class.java)

//        signupVM = SignupVM(this)
        binding.signupVM = signupVM
        signup = Signup()
        binding.signup = signup

        signUpResponseObserver()
        loadingObserver()
        languageChange()
        setToolbar()
        isAlreadyRegisteredPhoneObserver()
        signupClickObserver()
        checkPhoneNo()
        setInputValidation()
        showApiMsgObserver()
        showUserWarningObserver()
        allTownObserver()
//        initLocationUpdates()
        setTownsAdapter()
        hideKeyboardOnSpinnerClick()
    }

    private fun setTownsAdapter() {
        townAdapter = ArrayAdapter<String>(
            this,
            R.layout.row_spinner,
            townList
        )
        binding.spnTown.adapter = townAdapter
        binding.spnTown.onItemSelectedListener = spinnerItemSelection
    }

    val spinnerItemSelection = object : MaterialSpinner.OnItemSelectedListener {
        override fun onItemSelected(
            parent: MaterialSpinner,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (parent.id) {
                binding.spnTown.id -> {
                    val selectedCity = townAdapter?.getItem(position) ?: ""

                    var townId = ""
                    for (townWithId in allTownsWithIdList) {
                        if (selectedCity.equals(townWithId.value)) {
                            townId = townWithId.option_id ?: ""
                        }
                    }
                    signup?.town = townId
                }
            }
        }

        override fun onNothingSelected(parent: MaterialSpinner) {}
    }

/*    override fun onResume() {
        super.onResume()

        // Check location permission and enable GPS
        if (checkAndRequestPermissionLocation()) {
            if (UtilsFunctions.checkGpsEnabled(this)) {
            }
        }
    }*/

    private fun setInputValidation() {
//        binding.etName.filters = arrayOf<InputFilter>(UtilsFunctions.filterCharactersForName())
    }

    private fun checkPhoneNo() {
        binding.etMobileNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.toString().length == 10) {
                    // Check if already registered
                    signupVM.phoneNoCheck(text.toString())
                }
            }
        })
    }

    private fun setToolbar() {
        binding.rlToolbar.tvCommonHeading.setText(resources.getString(R.string.register_heading))
    }

    private fun signUpResponseObserver() {
        signupVM.signupResponse().observe(this, Observer {
            stopProgressDialog()


            if (it != null) {
//                finish()
//                UtilsFunctions.showToastSuccess("Signed up successfully $it")

                val dialog = DialogClass().setConfirmationDialog(
                    this,
                    "SignedUp",
                    R.layout.dialog_thanks_for_signup
                )

                val btnOk = dialog.findViewById<Button>(R.id.btnOK)
                btnOk.setOnClickListener { finish() }
                dialog.show()
            }
        })
    }

    private fun languageChange() {
        binding.tvViewInHindi.setOnClickListener {

        }
    }

    private fun loadingObserver() {
        signupVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun isAlreadyRegisteredPhoneObserver() {
        signupVM.isValidPhone().observe(this, Observer { isAlreadyRegisteredPhone ->
            stopProgressDialog()

            if (isAlreadyRegisteredPhone != null) {
//                UtilsFunctions.showToastSuccess(isAlreadyRegisteredPhone.message ?: "")
            } else {
                binding.etMobileNo.setText("")
                binding.etMobileNo.requestFocus()
            }
        })
    }

    private fun signupClickObserver() {
        signupVM.onSignupClick().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
//                UtilsFunctions.showToastSuccess("Verify your OTP now")

                var mobile = ""
                for (customAttribute in it.customer.custom_attributes) {
                    if (customAttribute.attribute_code.equals("phone_number")) {
                        mobile = customAttribute.value
                    }
                }
                startActivity(
                    Intent(this, SignupOtpVerifyActivity::class.java).putExtra(
                        "mobile",
                        mobile
                    )
                        .putExtra("customerObj", it)
                )
            }
        })
    }


    private fun showApiMsgObserver() {
        signupVM.showApiMsg().observe(this, Observer { msg ->
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

    private fun showUserWarningObserver() {
        signupVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                SignupHelper.setUserMsg(this, msg)
            }
        })
    }

    private fun allTownObserver() {
        signupVM.allTowns().observe(this, Observer { allTowns ->
            if (allTowns != null) {
                if (allTowns.size > 0) {
                    MyApplication.sharedPref.save(PreferenceKeys.ALL_TOWNS, allTowns)

                    // Town with ID list
                    allTownsWithIdList.clear()
                    allTownsWithIdList.addAll(allTowns)

                    val towns = ArrayList<String>()
                    for (town in allTowns) {
                        towns.add(town.value ?: "")
                    }

                    // Clear all previous data and add new
                    townList.clear()
                    townList.addAll(towns)
                    townAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun hideKeyboardOnSpinnerClick() {
        binding.spnTown.setOnFocusChangeListener(object: View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, isFocused: Boolean) {
                if (isFocused) {
                    hideKeyboard(this@SignupActivity)
                }
            }
        })
    }


    /**
     *  Get location
     */
    private fun initLocationUpdates() {
        fusedLocationClass = FusedLocationClass(this, this)
//        fusedLocationClass = fusedLocation
        val lastLoc = fusedLocationClass?.getLastLocation(this)

        if (lastLoc != null) {
            setCityAndStopLocUpdates(lastLoc.latitude, lastLoc.longitude)
        }
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lon = location.longitude

//        UtilsFunctions.showToastSuccess("latlon - > $lat $lon")

        setCityAndStopLocUpdates(lat, lon)
    }

    private fun setCityAndStopLocUpdates(lat: Double, lon: Double) {
        if (lat != 0.0 && lon != 0.0) {
            city = UtilsFunctions.getCityFromGeocode(this, lat, lon)

            if (!city.isEmpty()) {
                binding.etTown.setText(city)
                stopTracking()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        stopTracking()
        fusedLocationClass = null
    }

    private fun stopTracking() {
        if (fusedLocationClass != null) {
            fusedLocationClass?.stopLocationUpdates()
        }
    }
}