package com.seasia.poojasarees.repository.products

import androidx.lifecycle.MutableLiveData
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.response.products.ProductDetailsOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject

class ProductDetailsRepo {
    var productDetailsData: MutableLiveData<ProductDetailsOut>? = null
    val sessionExpire: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        productDetailsData = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun getProductDetailsResponse(productId: String, hit: Boolean): MutableLiveData<ProductDetailsOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()

//            val params = HashMap<String, String>()
//            params.put("searchCriteria[filterGroups][0][filters][0][field]", "entity_id")
//            params.put("searchCriteria[filterGroups][0][filters][0][condition_type]", "eq")
//            params.put("searchCriteria[filterGroups][0][filters][0][value]", productId)
            val call = service.getProductById(productId)

            call.enqueue(object : retrofit2.Callback<ProductDetailsOut> {
                override fun onFailure(call: retrofit2.Call<ProductDetailsOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    productDetailsData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ProductDetailsOut>,
                    response: retrofit2.Response<ProductDetailsOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            productDetailsData!!.value = response.body()
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
                            productDetailsData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            productDetailsData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return productDetailsData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }
}