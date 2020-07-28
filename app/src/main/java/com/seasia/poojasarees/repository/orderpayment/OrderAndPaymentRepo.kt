package com.seasia.poojasarees.repository.orderpayment

import androidx.lifecycle.MutableLiveData
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.request.shipping.PaymentInfoIn
import com.seasia.poojasarees.model.response.orderpayment.PlaceOrderIn
import com.seasia.poojasarees.model.response.shipping.SetShippingAndBillingAddressOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject

class OrderAndPaymentRepo {
    private var placeOrderData: MutableLiveData<String>? = null
    private var shippingAndBillingAddressData: MutableLiveData<SetShippingAndBillingAddressOut>? =
        null
    private val sessionExpire: MutableLiveData<Boolean>
    private val apiMsg: MutableLiveData<String>

    init {
        placeOrderData = MutableLiveData()
        shippingAndBillingAddressData = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun placeOrderResponse(placeOrderIn: PlaceOrderIn?, hit: Boolean): MutableLiveData<String> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.placeOrder(placeOrderIn)

            call.enqueue(object : retrofit2.Callback<String> {
                override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    placeOrderData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<String>,
                    response: retrofit2.Response<String>
                ) {
                    when (response.code()) {
                        200 -> {
                            placeOrderData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
                            }
                            placeOrderData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            placeOrderData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return placeOrderData!!
    }

    fun paymentMethodForOrderReponse(
        paymentInfoIn: PaymentInfoIn?,
        hit: Boolean
    ): MutableLiveData<SetShippingAndBillingAddressOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.selectPaymentMethodForOrder(paymentInfoIn)

            call.enqueue(object : retrofit2.Callback<SetShippingAndBillingAddressOut> {
                override fun onFailure(
                    call: retrofit2.Call<SetShippingAndBillingAddressOut>,
                    t: Throwable
                ) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    shippingAndBillingAddressData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<SetShippingAndBillingAddressOut>,
                    response: retrofit2.Response<SetShippingAndBillingAddressOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            shippingAndBillingAddressData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
                            }
                            shippingAndBillingAddressData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            shippingAndBillingAddressData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return shippingAndBillingAddressData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }
}