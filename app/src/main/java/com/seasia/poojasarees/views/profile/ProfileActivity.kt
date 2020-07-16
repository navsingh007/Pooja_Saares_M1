package com.seasia.poojasarees.views.profile

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.constants.AppConstants
import com.seasia.poojasarees.constants.ProfileConstants
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityProfileBinding
import com.seasia.poojasarees.helper.ProfileHelper
import com.seasia.poojasarees.model.helper.Profile
import com.seasia.poojasarees.model.response.ProfileOut
import com.seasia.poojasarees.utils.DialogClass
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.viewmodel.profile.ProfileVM
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class ProfileActivity : BaseActivity() {
    lateinit var binding: ActivityProfileBinding
    private lateinit var profileVM: ProfileVM
    private var profile: Profile? = null
    private lateinit var profileConstants: ProfileConstants

    //    private val profileGet: OnGetProfile? = null
    private var sectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_profile
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityProfileBinding

        profileVM = ViewModelProvider(this).get(ProfileVM::class.java)
//        profileVM = ProfileVM(this)
        binding.profileVM = profileVM

        profileConstants = ProfileConstants(this)

        profile = Profile()
        binding.prof = profile

        setTabsViewPager()
        setToolbar()
//        loadingObserver()
        profileVM.onGetProfile()
        updateProfileObserver()
        sessionExpireObserver()
        showUserWarningObserver()

        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.rlToolbar.ivBackPress.setOnClickListener {
            UtilsFunctions.hideKeyboardActivity(this)
            finish()
        }

        moveViewToCenterOnKeyboardOpen()
    }

    private fun setTabsViewPager() {
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, profile)

        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private fun setToolbar() {
        binding.rlToolbar.tvCommonHeading.setText(resources.getString(R.string.profile_edit_heading))
    }


    private fun loadingObserver() {
        profileVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun sessionExpireObserver() {
        profileVM.isSessionExpire().observe(this, Observer { sessionExpire ->
            if (sessionExpire) {
                UtilsFunctions.sessionExpired(this)
            } else {
                // Do nothing, session maintained
            }
        })
    }

    private fun updateProfileObserver() {
        profileVM.updateProfileResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
//                UtilsFunctions.showToastSuccess(resources.getString(R.string.profile_updated))

                // Save Gender
                MyApplication.sharedPref.saveString(PreferenceKeys.GENDER, it.gender?.toString() ?: "")

                // Save user name
                MyApplication.sharedPref.saveString(PreferenceKeys.FIRST_NAME, it.firstname ?: "")
                MyApplication.sharedPref.saveString(PreferenceKeys.LAST_NAME, it.lastname ?: "")

                // Save updated profile address ID as default
                val addresses = it.addresses
                if (addresses != null && !addresses.isEmpty()) {
                    for (address in addresses) {
                        if (address.default_billing != null && address.default_billing) {
                            MyApplication.sharedPref.saveString(
                                PreferenceKeys.DEFAULT_ADDRESS_ID,
                                address.id?.toString() ?: ""
                            )
                            break
                        }
                    }
                }

//                profileUpdatedSuccessDialog()

                UtilsFunctions.showToastSuccess(getString(R.string.profile_updated))
                finish()
            }
        })
    }

    private fun profileUpdatedSuccessDialog() {
        val dialog = DialogClass().commonConfirmationDialog(
            this,
            getString(R.string.profile_updated_title),
            getString(R.string.profile_updated_title),
            getString(R.string.profile_updated)
        )
        val btnOk = dialog.findViewById<Button>(R.id.btnOK)
        btnOk.setOnClickListener { finish() }
        dialog.show()
    }

/*    private fun getProfileObserver() {
        profileVM.getProfileResponse().observe(this, Observer {
            if (it != null) {
                val contactFrag =
                    supportFragmentManager.findFragmentByTag("android:switcher:" + binding.viewPager + ":0") as ProfileContactFragment
//                supportFragmentManager.findFragmentByTag("android:switcher:" + binding.viewPager +  ":1")

                val contactFragment = sectionsPagerAdapter!!.getItem(0) as ProfileContactFragment
//                contactFragment.updateTextView(it)

//                contactFragment.etName.setText("Navdeep")
//                contactFragment.etShop.setText("pooja")
//                contactFragment.etMobileNo.setText("91834937")

//                (contactFragment as OnGetProfile).setProfileDetail(it)

                val profileFragment = sectionsPagerAdapter!!.getItem(1) as ProfilePersonalFragment
//                (profileFragment as OnGetProfile).setProfileDetail(it)
            }
        })
    }*/

    private fun showUserWarningObserver() {
        profileVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                ProfileHelper.setUserMsg(this, msg)
            }
        })
    }

    interface OnGetProfile {
        fun setProfileDetail(profleOut: ProfileOut)
    }




    /**
     *  Codes and values
     */

    fun getMaritalStatusCode(maritalStatus: String): String {
        return when (maritalStatus) {
            profileConstants.STATUS_MARRIED -> AppConstants.CODE_MARRIED
            profileConstants.STATUS_UNMARRIED -> AppConstants.CODE_UNMARRIED
            profileConstants.STATUS_DIVORCED -> AppConstants.CODE_DIVORCED
            else -> ""
        }
    }

    fun getMaritalStatusValue(maritalStatus: String): String {
        return when (maritalStatus) {
            AppConstants.CODE_MARRIED -> profileConstants.STATUS_MARRIED
            AppConstants.CODE_UNMARRIED -> profileConstants.STATUS_UNMARRIED
            AppConstants.CODE_DIVORCED -> profileConstants.STATUS_DIVORCED
            else -> ""
        }
    }

    fun getGenderCode(gender: String): String {
        return when (gender) {
            profileConstants.GENDER_MALE -> AppConstants.CODE_MALE
            profileConstants.GENDER_FEMALE -> AppConstants.CODE_FEMALE
            profileConstants.GENDER_OTHER -> AppConstants.CODE_OTHER
            else -> ""
        }
    }

    fun getGenderValue(gender: String): String {
        return when (gender) {
            AppConstants.CODE_MALE -> profileConstants.GENDER_MALE
            AppConstants.CODE_FEMALE -> profileConstants.GENDER_FEMALE
            AppConstants.CODE_OTHER -> profileConstants.GENDER_OTHER
            else -> ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        UtilsFunctions.hideKeyboardActivity(this)
    }

    private fun moveViewToCenterOnKeyboardOpen() {
        KeyboardVisibilityEvent.setEventListener(
            this,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {

                    // write your code
                    if (isOpen) {
                        binding.rlButtons.visibility = View.GONE
                    } else {
                        binding.rlButtons.postDelayed(Runnable { binding.rlButtons.visibility = View.VISIBLE }, 300)
                    }
                }
            })
    }
}