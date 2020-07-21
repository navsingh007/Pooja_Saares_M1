package com.seasia.poojasarees.views.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseFragment
import com.seasia.poojasarees.databinding.FragmentProfileContactBinding
import com.seasia.poojasarees.model.helper.Profile
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.profile.ProfileOut
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.profile.ProfileVM
import com.tiper.MaterialSpinner
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class ProfileContactFragment : BaseFragment() { //, ProfileActivity.OnGetProfile {
    private lateinit var binding: FragmentProfileContactBinding
    private lateinit var context: AppCompatActivity
    private lateinit var profileVM: ProfileVM
    private var profile: Profile? = null
    private var townList: ArrayList<String> = ArrayList()
    private var allTownsWithIdList: ArrayList<AllTownsOut> = ArrayList()
    private var townAdapter: ArrayAdapter<String>? = null
    private var allStatesInCountry: ArrayList<AllStatesOut.AvailableRegion>? = null

    private var townCity = ""

    // Spinner field
    private var stateResponse = ""

    private var myView: View? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_profile_contact
    }

    override fun initView() {
        binding = viewDataBinding as FragmentProfileContactBinding
        context = baseActivity
        profileVM = ViewModelProvider(baseActivity).get(ProfileVM::class.java)

//        view = getView()?.rootView?.getWindow
        myView = requireView().getRootView()!!


//        profileVM = ProfileVM(baseActivity)

//        binding.profileVM = profileVM

        profile = requireArguments().getSerializable(PROFILE_CONTACT_KEY) as Profile
        binding.profile = profile

        binding.spnState.onItemSelectedListener = spinnerItemSelection

//        allStatesObserver()
        loadingObserver()
//        profileVM.getAllStates()
        showApiMsgObserver()
//        profileVM.onGetProfile()
//        sessionExpireObserver()

//        binding.etGstNo.addTextChangedListener(tw)
        allTownObserver()
        setTownsAdapter()
        hideKeyboardOnSpinnerClick()
        moveViewToCenterOnKeyboardOpen()

//        adjustResizeProgramatically()
    }

    val tw = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.etGstNo.setSelection(binding.etGstNo.text.toString().length)
        }
    }

    private fun setTownsAdapter() {
/*        val allTownsRaw = MyApplication.sharedPref.getString(PreferenceKeys.ALL_TOWNS, "") ?: ""
        val allTowns = UtilsFunctions.getTowns(allTownsRaw)

        if (allTowns.size > 0) {
            val towns = ArrayList<String>()
            for (town in allTowns) {
                towns.add(town.value ?: "")
            }

            townList.clear()
            townList.addAll(towns)
        }*/

        townAdapter = ArrayAdapter<String>(
            baseActivity,
            R.layout.row_spinner,
            townList
        )

        binding.spnTown.adapter = townAdapter
        binding.spnTown.onItemSelectedListener = spinnerItemSelection
    }

    private fun allStatesObserver() {
        profileVM.getAllStatesResponse().observe(this, Observer {
//            baseActivity.stopProgressDialog()

            if (it != null && it[0].available_regions != null) {
                val allStates = it[0].available_regions
                allStatesInCountry = allStates

                if (allStates != null) {
                    val stateNames = ArrayList<String>()
                    for (state in allStates!!) {
                        stateNames.add(state.name!!)
                    }
                    val adapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        stateNames
                    )
                    binding.spnState.adapter = adapter

                    // Set user Profile selected state
                    for (region in allStates) {
                        if (stateResponse.equals(region.name)) {
                            binding.spnState.selection = adapter.getPosition(stateResponse)
                        }
                    }
                }
            }
        })
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

                    profile?.town = townId

                    binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_UP) })
                }
            }

//            allStatesInCountry?.let {
//                profile?.state = it[position].id ?: ""
//            }
        }

        override fun onNothingSelected(parent: MaterialSpinner) {}
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

    fun setProfileDetail(profleOut: ProfileOut) {
        // Contact
        profleOut.let {
            val userName = "${it.firstname}"  //${it.lastname}
            val userLastName = "${it.lastname}"  //${it.lastname}
            var shopName = ""
            var mobile = ""
            val email = it.email
            val gstNo = it.taxvat
            var town = ""
            var street = ""
            var state = ""
            var pincode = ""

            if (it.addresses != null && it.addresses.size > 0) {
                street = it.addresses[0].street?.component1() ?: ""
                state = it.addresses[0].region?.region ?: ""
                stateResponse = state
                pincode = it.addresses[0].postcode ?: ""
            }

            if (it.custom_attributes != null) {
                for (i in 0..it.custom_attributes.size - 1) {
                    val attributeCode = it.custom_attributes[i].attribute_code
                    val value = it.custom_attributes[i].value

                    if (attributeCode.equals("phone_number")) {
                        mobile = value ?: ""
                    } else if (attributeCode.equals("shopname")) {
                        shopName = value ?: ""
                    } else if (attributeCode.equals("town_city_dropdown")) {
                        town = value ?: ""
                    }
                }
            }

            // Set email id only if it is not default
            if (!email.equals("${mobile}_DEFAULT_ID@gmail.com")) {
                binding.etEmail.setText(email)
            }

            binding.etName.setText(userName)
            binding.etLastName.setText(userLastName)
            binding.etShop.setText(shopName)
            binding.etMobileNo.setText(mobile)
            binding.etGstNo.setText(gstNo)
            binding.etStreet.setText(street)
//            binding.etTown.setText(town)
//            binding.spnState.text.setText(state)
            binding.etPincode.setText(pincode)

            if (!town.isEmpty()) {
                var townName = ""
                for (townWithId in allTownsWithIdList) {
                    if (town.equals(townWithId.option_id)) {
                        townName = townWithId.value ?: ""

                        binding.spnTown.selection =
                            townAdapter!!.getPosition(townName)
                    }
                }
                townCity = townName
            }
        }
        setFocusAtEndOfEdittext()
    }

    private fun setFocusAtEndOfEdittext() {
        binding.etName.setSelection(binding.etName.text.toString().length)
        binding.etLastName.setSelection(binding.etLastName.text.toString().length)
        binding.etShop.setSelection(binding.etShop.text.toString().length)
        binding.etEmail.setSelection(binding.etEmail.text.toString().length)
        binding.etGstNo.setSelection(binding.etGstNo.text.toString().length)
    }

    private fun getProfileObserver() {
        profileVM.getProfileResponse().observe(this, Observer {
            baseActivity.stopProgressDialog()

            if (it != null) {
                setProfileDetail(it)
            }
        })
    }

    private fun sessionExpireObserver() {
        profileVM.isSessionExpire().observe(this, Observer { sessionExpire ->
            if (sessionExpire) {
                UtilsFunctions.sessionExpired(baseActivity)
            } else {
                // Do nothing, session maintained
            }
        })
    }

    private fun showApiMsgObserver() {
        profileVM.showApiMsg().observe(this, Observer { msg ->
            stopProgressDialog()

            if (msg != null) {
                if (msg.equals("SESSION_EXPIRED")) {
                    UtilsFunctions.showToastError(resources.getString(R.string.session_expire))
                } else {
                    UtilsFunctions.showToastError(msg)
                }
            }
        })
    }

    private fun allTownObserver() {
        profileVM.allTowns().observe(this, Observer { allTowns ->
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

//                    townCity

                    var isDefault = true
                    var townIndex = 0
                    for ((index, town) in towns.withIndex()) {
                        if (town.equals(townCity)) {
                            townIndex = index
                            isDefault = false
                        }
                    }

                    Log.d("ProfContFrag ======> ", "Inside Index - $townCity - $townIndex - $isDefault")
                    if (!isDefault) {
                        Log.d("ProfContFrag ======> ", "Outside Index - $townCity - $townIndex - $isDefault")
                        binding.spnTown.selection = townAdapter!!.getPosition(townCity)
                    }
//                    binding.spnTown.selection =
//                        townAdapter!!.getPosition(townCity)


                    // Now observe Profile
                    getProfileObserver()
                }
            }
        })
    }


    companion object {
        private val PROFILE_CONTACT_KEY = "profileContact"

        fun newInstance(profile: Profile?): Fragment? {
            val fragment = ProfileContactFragment()
            val bundle = Bundle()
            bundle.putSerializable(PROFILE_CONTACT_KEY, profile)
            fragment.setArguments(bundle)
            return fragment
        }
    }

    override fun onStop() {
        super.onStop()

//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, myView!!)

//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etName)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etLastName)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etShopHeading)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etMobileNo)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etEmail)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etGstNo)
//        UtilsFunctions.hideKeyboardFromFragment(baseActivity, binding.etTown)
    }


    private fun hideKeyboardOnSpinnerClick() {
        binding.spnTown.setOnFocusChangeListener(object: View.OnFocusChangeListener {
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


/*        binding.etEmail.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                binding.svRoot.smoothScrollTo(0, binding.etEmail.bottom)
                binding.svRoot.post(Runnable { binding.etEmail.requestFocus() }
                )
                return false
            }
        })

        binding.etGstNo.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                binding.svRoot.smoothScrollTo(0, binding.etGstNo.top)
                binding.svRoot.post(Runnable { binding.etGstNo.requestFocus() }
                )
                return false
            }
        })*/
    }

    private fun adjustResizeProgramatically() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}