package com.seasia.poojasarees.helper

import android.content.Context
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.viewmodel.auth.SignupVM

object SignupHelper {

    fun setUserMsg(context: Context, msg: String) {
        when (msg) {
            SignupVM.ENTER_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_name))
            SignupVM.ENTER_LAST_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_last_name))
            SignupVM.ENTER_TOWN -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_town))
            SignupVM.ENTER_MOBILE_NO -> UtilsFunctions.showToastWarning(
                context.resources.getString(
                    R.string.register_warn_mobile_no
                )
            )
            SignupVM.ENTER_PASSWORD -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_password))
            SignupVM.WARN_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_valid_name))
            SignupVM.WARN_LAST_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_valid_last_name))
            SignupVM.WARN_TOWN -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_valid_town))
            SignupVM.WARN_MOBILE_NO -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_valid_mobile_no))
            SignupVM.MIN_MOBILE -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_mobile))
            SignupVM.MIN_SHOP_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_shopName))
            SignupVM.MIN_PASSWORD -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_warn_password_hint))
            SignupVM.SELECT_TOWN -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.register_select_town))
            SignupVM.INVALID_PASSWORD -> UtilsFunctions.showToastWarning(
                context.resources.getString(
                    R.string.register_warn_password_chars
                )
            )
        }
    }

    fun setApiMsg(msg: String) {

    }
}