package com.seasia.poojasarees.helperlocalize

import android.content.Context
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.viewmodel.profile.ProfileVM
import com.seasia.poojasarees.views.profile.ProfileActivity

object ProfileHelper {

    fun setUserMsg(context: Context, msg: String) {
        when (msg) {
            ProfileVM.ENTER_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_name))
            ProfileVM.ENTER_LAST_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_last_name))
            ProfileVM.ENTER_EMAIL -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.profile_enter_email))
//            ProfileVM.ENTER_TOWN -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_town))
            ProfileVM.ENTER_MOBILE_NO -> UtilsFunctions.showToastWarning(
                context.resources.getString(
                    R.string.register_warn_mobile_no
                )
            )
            ProfileVM.MIN_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.profile_valid_name))
            ProfileVM.MIN_LAST_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.profile_valid_last_name))
            ProfileVM.MIN_GST -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.profile_valid_gst))
            ProfileVM.MIN_SHOP -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_shopName))
            ProfileVM.MIN_MOBILE_NO -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_mobile))
            ProfileVM.VALID_EMAIL -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.profile_valid_email))
            ProfileVM.SELECT_TOWN -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.profile_select_town))
        }

        // Go to contacts tab
        (context as ProfileActivity).binding.tabs.getTabAt(0)?.select()
        (context as ProfileActivity).binding.rlRoot.isFocusable = true
        (context as ProfileActivity).binding.rlRoot.isFocusableInTouchMode = true
    }

    fun setApiMsg(msg: String) {

    }
}