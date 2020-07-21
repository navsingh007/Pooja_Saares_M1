package com.seasia.poojasarees.helperlocalize

import android.content.Context
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.viewmodel.address.AddressVM

object AddAddressHelper {

    fun setUserMsg(context: Context, msg: String) {
        when (msg) {
            AddressVM.ENTER_SHOP_NAME -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_enter_shop_name))
            AddressVM.ENTER_STREET -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_enter_street))
            AddressVM.ENTER_TOWN -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_enter_town))
            AddressVM.ENTER_DISTRICT -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_enter_district))
            AddressVM.ENTER_PIN_CODE -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_enter_pincode))
            AddressVM.SELECT_STATE -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_select_state))
            AddressVM.MIN_SHOP -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_min_shop))
            AddressVM.MIN_STREET -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_min_street))
            AddressVM.MIN_TOWN -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_min_town))
            AddressVM.MIN_DISTRICT -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_min_district))
            AddressVM.MIN_PINCODE -> UtilsFunctions.showToastWarning(context.resources.getString(
                    R.string.address_min_pincode
                ))
            AddressVM.INVALID_PINCODE -> UtilsFunctions.showToastWarning(context.resources.getString(R.string.address_invalid_pincode))
        }
    }

    fun setApiMsg(msg: String) {

    }
}