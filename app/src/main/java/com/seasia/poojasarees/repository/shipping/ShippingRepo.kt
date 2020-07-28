package com.seasia.poojasarees.repository.shipping

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.request.shipping.PaymentInfoIn
import com.seasia.poojasarees.model.request.shipping.ShippingInfoIn
import com.seasia.poojasarees.model.response.shipping.AvailableShippingMethodOut
import com.seasia.poojasarees.model.response.shipping.SetShippingAndBillingAddressOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import java.util.ArrayList

class ShippingRepo {
    private var availableShippingMethodData: MutableLiveData<ArrayList<AvailableShippingMethodOut>>? = null
    private var shippingAndBillingAddressData: MutableLiveData<SetShippingAndBillingAddressOut>? = null
    private val sessionExpire: MutableLiveData<Boolean>
    private val apiMsg: MutableLiveData<String>

    init {
        availableShippingMethodData = MutableLiveData()
        shippingAndBillingAddressData = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun availableShippingMethodsResponse(shippingMethodsIn: JsonObject?, hit: Boolean): MutableLiveData<ArrayList<AvailableShippingMethodOut>> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.getAvailableShippingMethodsByAddressId(shippingMethodsIn)

            call.enqueue(object : retrofit2.Callback<ArrayList<AvailableShippingMethodOut>> {
                override fun onFailure(call: retrofit2.Call<ArrayList<AvailableShippingMethodOut>>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    availableShippingMethodData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ArrayList<AvailableShippingMethodOut>>,
                    response: retrofit2.Response<ArrayList<AvailableShippingMethodOut>>
                ) {
                    when (response.code()) {
                        200 -> {
                            availableShippingMethodData!!.value = response.body()
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
                            availableShippingMethodData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            availableShippingMethodData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return availableShippingMethodData!!
    }

    fun setShippingAndBillingForCartReponse(
        shippingInfoIn: ShippingInfoIn?,
        hit: Boolean
    ): MutableLiveData<SetShippingAndBillingAddressOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.setShippingAndBillingAddressForCart(shippingInfoIn)

            call.enqueue(object : retrofit2.Callback<SetShippingAndBillingAddressOut> {
                override fun onFailure(call: retrofit2.Call<SetShippingAndBillingAddressOut>, t: Throwable) {
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

/*    fun paymentMethodForOrderReponse(
        paymentInfoIn: PaymentInfoIn?,
        hit: Boolean
    ): MutableLiveData<SetShippingAndBillingAddressOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.selectPaymentMethodForOrder(paymentInfoIn)

            call.enqueue(object : retrofit2.Callback<SetShippingAndBillingAddressOut> {
                override fun onFailure(call: retrofit2.Call<SetShippingAndBillingAddressOut>, t: Throwable) {
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
    }*/

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }
}