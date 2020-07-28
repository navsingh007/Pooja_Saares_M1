package com.seasia.poojasarees.views.profile

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.constants.ProfileConstants
import com.seasia.poojasarees.core.BaseFragment
import com.seasia.poojasarees.databinding.FragmentProfilePersonalBinding
import com.seasia.poojasarees.model.helper.Profile
import com.seasia.poojasarees.model.response.ProfileOut
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.profile.ProfileVM
import com.tiper.MaterialSpinner
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.text.SimpleDateFormat
import java.util.*

class ProfilePersonalFragment : BaseFragment() { //, ProfileActivity.OnGetProfile {
    private lateinit var binding: FragmentProfilePersonalBinding
    private lateinit var context: AppCompatActivity
    private lateinit var profileVM: ProfileVM
    private var profile: Profile? = null
    private lateinit var GENDER_LIST: ArrayList<String>
    private lateinit var MARITAL_STATUS_LIST: ArrayList<String>

    // Adapters
    private var genderAdapter: ArrayAdapter<String>? = null
    private var maritalStatusAdapter: ArrayAdapter<String>? = null

/*    private val GENDER_LIST =
        arrayListOf(AppConstants.GENDER_MALE, AppConstants.GENDER_FEMALE, AppConstants.GENDER_OTHER)

    private val MARITAL_STATUS_LIST =
        arrayListOf(
            AppConstants.STATUS_MARRIED,
            AppConstants.STATUS_UNMARRIED,
            AppConstants.STATUS_DIVORCED
        )*/

    override fun getLayoutResId(): Int {
        return R.layout.fragment_profile_personal
    }

    override fun initView() {
        binding = viewDataBinding as FragmentProfilePersonalBinding
        context = baseActivity
        profileVM = ViewModelProvider(baseActivity).get(ProfileVM::class.java)
//        profileVM = ProfileVM(baseActivity)

//        binding.profileVM = profileVM

        profile = requireArguments().getSerializable(PROFILE_PERSONAL_KEY) as Profile
        binding.profile = profile

        val profileConstants = ProfileConstants(baseActivity)
        GENDER_LIST =
            arrayListOf(
                profileConstants.GENDER_MALE,
                profileConstants.GENDER_FEMALE,
                profileConstants.GENDER_OTHER
            )

        genderAdapter = ArrayAdapter<String>(
            context,
            R.layout.row_spinner,
            GENDER_LIST
        )

        binding.spnGender.adapter = genderAdapter

        MARITAL_STATUS_LIST =
            arrayListOf(
                profileConstants.STATUS_MARRIED,
                profileConstants.STATUS_UNMARRIED,
                profileConstants.STATUS_DIVORCED
            )
        maritalStatusAdapter = ArrayAdapter<String>(
            context,
            R.layout.row_spinner,
            MARITAL_STATUS_LIST
        )
        binding.spnMaritalStatus.adapter = maritalStatusAdapter

        binding.spnGender.onItemSelectedListener = spinnerItemSelection
        binding.spnMaritalStatus.onItemSelectedListener = spinnerItemSelection

        getProfileObserver()
//        profileVM.onGetProfile()
        loadingObserver()
        showApiMsgObserver()
        selectDob()
        hideKeyboardOnSpinnerClick()
//        moveViewToCenterOnKeyboardOpen()
    }

    private fun selectDob() {
        var strDate = ""
        binding.etDob.setOnClickListener { view ->
            val calendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "yyyy-MM-dd"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    strDate = sdf.format(calendar.time)
                    binding.etDob.setText(strDate)


                    // Check age greater than 14

//                    if (calculateAge(calendar.timeInMillis) < 15) {
//                        UtilsFunctions.showToastWarning(resources.getString(R.string.profile_not_valid_dob))
//                        binding.etDob.setText("")
//                    }
                }

//            R.style.datePickerStyle,

            val dpDialog = DatePickerDialog(
                baseActivity, date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Disable future dates
            dpDialog.datePicker.maxDate = System.currentTimeMillis() - 1000

            // Open datepicker with specific date
            val dob = binding.etDob.text.toString()
            if (!dob.isEmpty()) {
                val selectedDate = dob.split("-").toTypedArray()

                val year = selectedDate.get(0).toInt() /* Year */
                val month = selectedDate.get(1).toInt() /* Month */
                val day = selectedDate.get(2).toInt() /* Day */

                dpDialog.datePicker.updateDate(
                    year,
                    month - 1,
                    day
                )
            }

            dpDialog.setOnShowListener {
                dpDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(resources.getColor(R.color.loginColor))
                dpDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(resources.getColor(R.color.loginColor))
                dpDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL)
                    .setTextColor(resources.getColor(R.color.loginColor))
            }

            dpDialog.setTitle("")
            dpDialog.show()
        }
    }

    val spinnerItemSelection = object : MaterialSpinner.OnItemSelectedListener {
        override fun onItemSelected(
            parent: MaterialSpinner,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (parent.id) {
                binding.spnGender.id -> {
                    profile?.gender =
                        (baseActivity as ProfileActivity).getGenderCode(GENDER_LIST[position])
//                    UtilsFunctions.showToastSuccess(
//                        "gender selected - ${UtilsFunctions.getGenderCode(
//                            GENDER_LIST[position]
//                        )}"
//                    )
                }
                binding.spnMaritalStatus.id -> {
                    profile?.maritalStatus =
                        (baseActivity as ProfileActivity).getMaritalStatusCode(MARITAL_STATUS_LIST[position])
//                    UtilsFunctions.showToastSuccess(
//                        "marital status selected - ${UtilsFunctions.getMaritalStatusCode(
//                            MARITAL_STATUS_LIST[position]
//                        )}"
//                    )
                }
            }
        }

        override fun onNothingSelected(parent: MaterialSpinner) {}
    }

    private fun getProfileObserver() {
        profileVM.getProfileResponse().observe(this, Observer {
            baseActivity.stopProgressDialog()

            if (it != null) {
                setProfileDetail(it)
            }
        })
    }

    fun setProfileDetail(profleOut: ProfileOut) {
        // Personal
        val dob = profleOut.dob
        val gender = (baseActivity as ProfileActivity).getGenderValue(
            MyApplication.sharedPref.getString(
                PreferenceKeys.GENDER,
                ""
            ) ?: ""
        )
//        val gender = UtilsFunctions.getGenderValue("${profleOut.gender}")
        var maritalStatus = ""
        var qualification = ""
        var languageKnown = ""
        var hobbies = ""

        if (profleOut.custom_attributes != null) {
            for (i in 0..profleOut.custom_attributes.size - 1) {
                val attributeCode = profleOut.custom_attributes[i].attribute_code
                val value = profleOut.custom_attributes[i].value

                if (attributeCode.equals("marital_status")) {
                    maritalStatus = value ?: ""
                } else if (attributeCode.equals("qualification")) {
                    qualification = value ?: ""
                } else if (attributeCode.equals("language_known")) {
                    languageKnown = value ?: ""
                } else if (attributeCode.equals("hobbies")) {
                    hobbies = value ?: ""
                }
            }
        }

        if (!gender.isEmpty()) {
            binding.spnGender.selection =
                genderAdapter!!.getPosition(gender)
        }
        if (!maritalStatus.isEmpty()) {
//            binding.spnMaritalStatus.selection = genderAdapter!!.getPosition(AppConstants.GENDER_MALE)
            binding.spnMaritalStatus.selection = maritalStatusAdapter!!.getPosition(
                (baseActivity as ProfileActivity).getMaritalStatusValue(maritalStatus)
            )
        }

        binding.etDob.setText(dob)
//        binding.spnGender.text.setText(gender)
//        binding.spnMaritalStatus.text.setText(maritalStatus)
        binding.etEducationalQualifications.setText(qualification)
        binding.etLanguageKnown.setText(languageKnown)
        binding.etHobby.setText(hobbies)

        setFocusAtEndOfEdittext()
    }

    private fun setFocusAtEndOfEdittext() {
        binding.etEducationalQualifications.setSelection(binding.etEducationalQualifications.text.toString().length)
        binding.etLanguageKnown.setSelection(binding.etLanguageKnown.text.toString().length)
        binding.etHobby.setSelection(binding.etHobby.text.toString().length)
    }

    private fun loadingObserver() {
        profileVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                baseActivity.startProgressDialog()
            } else {
                baseActivity.stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        profileVM.showApiMsg().observe(this, Observer { msg ->
            if (msg != null) {
                if (msg.equals("SESSION_EXPIRED")) {
                    UtilsFunctions.showToastError(resources.getString(R.string.session_expire))
                } else {
                    UtilsFunctions.showToastError(msg)
                }
            }
        })
    }

    companion object {
        private val PROFILE_PERSONAL_KEY = "profilePersonal"

        fun newInstance(profile: Profile?): Fragment? {
            val fragment = ProfilePersonalFragment()
            val bundle = Bundle()
            bundle.putSerializable(PROFILE_PERSONAL_KEY, profile)
            fragment.setArguments(bundle)
            return fragment
        }
    }

    override fun onStop() {
        super.onStop()

//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etDob)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.spnGender)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.spnMaritalStatus)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etEducationalQualifications)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etLanguageKnown)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etHobby)
    }

    fun calculateAge(date: Long): Int {
        val dob = Calendar.getInstance()
        dob.timeInMillis = date
        val today = Calendar.getInstance()
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_MONTH] < dob[Calendar.DAY_OF_MONTH]) {
            age--
        }
        return age
    }


    private fun hideKeyboardOnSpinnerClick() {
        binding.spnGender.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, isFocused: Boolean) {
                if (isFocused) {
                    UtilsFunctions.hideKeyboardActivity(baseActivity)
                }
            }
        })

        binding.spnMaritalStatus.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, isFocused: Boolean) {
                if (isFocused) {
                    UtilsFunctions.hideKeyboardActivity(baseActivity)
                }
            }
        })
    }

    /**
     *  On TAB change hide keyboard
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            try {
                val mImm: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                mImm.hideSoftInputFromWindow(requireView().windowToken, 0)
                mImm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                Log.e("ProfileFrag: ", "setUserVisibleHint: ", e)
            }
        }
    }

    private fun moveViewToCenterOnKeyboardOpen() {
        KeyboardVisibilityEvent.setEventListener(
            baseActivity,
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
    }
}