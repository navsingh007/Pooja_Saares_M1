package com.seasia.poojasarees.viewmodel.orderpayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.request.shipping.PaymentInfoIn
import com.seasia.poojasarees.model.response.cart.AddToCartOut
import com.seasia.poojasarees.model.response.orderpayment.PlaceOrderIn
import com.seasia.poojasarees.model.response.shipping.SetShippingAndBillingAddressOut
import com.seasia.poojasarees.repository.orderpayment.OrderAndPaymentRepo

class OrderAndPaymentVM: ViewModel() {
    private var orderAndPaymentRepo: OrderAndPaymentRepo
    private var paymentMethodForOrderData: MutableLiveData<SetShippingAndBillingAddressOut>
    private var placeOrderData: MutableLiveData<String>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        orderAndPaymentRepo = OrderAndPaymentRepo()
        paymentMethodForOrderData = MutableLiveData<SetShippingAndBillingAddressOut>()
        placeOrderData = MutableLiveData<String>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        paymentMethodForOrderData = orderAndPaymentRepo.paymentMethodForOrderReponse(null, false)
        placeOrderData = orderAndPaymentRepo.placeOrderResponse(null, false)
    }

    fun paymentMethodForOrder() {
        if (UtilsFunctions.isNetworkConnected()) {
            val paymentInfoIn = PaymentInfoIn()

            paymentMethodForOrderData = orderAndPaymentRepo.paymentMethodForOrderReponse(paymentInfoIn,true)
            isLoading.postValue(true)
        }
    }

    fun placeOrder() {
        if (UtilsFunctions.isNetworkConnected()) {
            val placeOrderIn = PlaceOrderIn()

            placeOrderData = orderAndPaymentRepo.placeOrderResponse(placeOrderIn, true)
            isLoading.postValue(true)
        }
    }

    fun onPaymentMethodForOrder(): LiveData<SetShippingAndBillingAddressOut> {
        return paymentMethodForOrderData
    }

    fun onPlaceOrder(): LiveData<String> {
        return placeOrderData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return orderAndPaymentRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return orderAndPaymentRepo.isSessionExpire()
    }
}