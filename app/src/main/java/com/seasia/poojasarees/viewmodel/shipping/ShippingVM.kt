package com.seasia.poojasarees.viewmodel.shipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.request.cart.AddToCartIn
import com.seasia.poojasarees.model.request.shipping.PaymentInfoIn
import com.seasia.poojasarees.model.request.shipping.ShippingInfoIn
import com.seasia.poojasarees.model.response.cart.AddToCartOut
import com.seasia.poojasarees.model.response.shipping.AvailableShippingMethodOut
import com.seasia.poojasarees.model.response.shipping.SetShippingAndBillingAddressOut
import com.seasia.poojasarees.repository.shipping.ShippingRepo
import java.util.ArrayList

class ShippingVM: ViewModel() {
    private var shippingRepo: ShippingRepo
    private var availableShippingMethodData: MutableLiveData<ArrayList<AvailableShippingMethodOut>>
    private var shippingAndBillingAddressData: MutableLiveData<SetShippingAndBillingAddressOut>
    private var selectPaymentMethodData: MutableLiveData<SetShippingAndBillingAddressOut>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        shippingRepo = ShippingRepo()
        availableShippingMethodData = MutableLiveData<ArrayList<AvailableShippingMethodOut>>()
        shippingAndBillingAddressData = MutableLiveData<SetShippingAndBillingAddressOut>()
        selectPaymentMethodData = MutableLiveData<SetShippingAndBillingAddressOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        availableShippingMethodData = shippingRepo.availableShippingMethodsResponse(null, false)
        shippingAndBillingAddressData = shippingRepo.setShippingAndBillingForCartReponse(null, false)
//        selectPaymentMethodData = shippingRepo.paymentMethodForOrderReponse(null, false)
    }

    fun avaialableShippingMethods(addressId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            val shippingMethodIn = JsonObject()
            shippingMethodIn.addProperty("addressId", addressId)
            availableShippingMethodData = shippingRepo.availableShippingMethodsResponse(shippingMethodIn, true)
            isLoading.postValue(true)
        }
    }

    fun setShippingAndBillingAddress() {
        if (UtilsFunctions.isNetworkConnected()) {
            val shippingInfoIn = ShippingInfoIn()

            shippingAndBillingAddressData = shippingRepo.setShippingAndBillingForCartReponse(shippingInfoIn, true)
            isLoading.postValue(true)
        }
    }

/*    fun setPaymentMethodsForOrder() {
        if (UtilsFunctions.isNetworkConnected()) {
            val paymentInfoIn = PaymentInfoIn()

            selectPaymentMethodData = shippingRepo.paymentMethodForOrderReponse(paymentInfoIn, true)
            isLoading.postValue(true)
        }
    }*/

    fun getShippingMethods(): LiveData<ArrayList<AvailableShippingMethodOut>> {
        return availableShippingMethodData
    }

    fun onSetShippingAndBilling(): LiveData<SetShippingAndBillingAddressOut> {
        return shippingAndBillingAddressData
    }

    fun onPaymentMethoSelect(): LiveData<SetShippingAndBillingAddressOut> {
        return selectPaymentMethodData
    }
    
    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return shippingRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return shippingRepo.isSessionExpire()
    }
}