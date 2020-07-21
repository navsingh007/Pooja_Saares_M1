package com.seasia.poojasarees.repository.cart

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.request.cart.AddToCartIn
import com.seasia.poojasarees.model.response.cart.AddToCartOut
import com.seasia.poojasarees.model.response.cart.CustomerByCartIdOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject

class CartRepo {
    private var createCartData: MutableLiveData<Int>? = null
    private var addToCartData: MutableLiveData<AddToCartOut>? = null
    private var getCustByCartIdData: MutableLiveData<CustomerByCartIdOut>? = null
    private val sessionExpire: MutableLiveData<Boolean>
    private val apiMsg: MutableLiveData<String>

    init {
        createCartData = MutableLiveData()
        addToCartData = MutableLiveData()
        getCustByCartIdData = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun createCartResponse(hit: Boolean): MutableLiveData<Int> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.createCustCart()

            call.enqueue(object : retrofit2.Callback<Int> {
                override fun onFailure(call: retrofit2.Call<Int>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    createCartData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<Int>,
                    response: retrofit2.Response<Int>
                ) {
                    when (response.code()) {
                        200 -> {
                            createCartData!!.value = response.body()
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
                            createCartData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            createCartData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return createCartData!!
    }

    fun addToCartReponse(
        addToCartIn: AddToCartIn?,
        hit: Boolean
    ): MutableLiveData<AddToCartOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.addItemsToCart(addToCartIn)

            call.enqueue(object : retrofit2.Callback<AddToCartOut> {
                override fun onFailure(call: retrofit2.Call<AddToCartOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    addToCartData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<AddToCartOut>,
                    response: retrofit2.Response<AddToCartOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            addToCartData!!.value = response.body()
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
                            addToCartData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            addToCartData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return addToCartData!!
    }

    fun getCustomerByCartIdResponse(
        cartId: String?,
        hit: Boolean
    ): MutableLiveData<CustomerByCartIdOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getCustCartById(cartId)

            call.enqueue(object : retrofit2.Callback<CustomerByCartIdOut> {
                override fun onFailure(call: retrofit2.Call<CustomerByCartIdOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    getCustByCartIdData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<CustomerByCartIdOut>,
                    response: retrofit2.Response<CustomerByCartIdOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            getCustByCartIdData!!.value = response.body()
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
                            getCustByCartIdData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            getCustByCartIdData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return getCustByCartIdData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }
}