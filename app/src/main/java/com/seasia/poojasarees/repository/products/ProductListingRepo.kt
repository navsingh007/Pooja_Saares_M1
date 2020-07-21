package com.seasia.poojasarees.repository.products

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.response.products.ProductFilterAttributes
import com.seasia.poojasarees.model.response.products.ProductsByCategoryIdOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject

class ProductListingRepo {
    var productListingData: MutableLiveData<ProductsByCategoryIdOut>? = null
    var filtersAttributesData: MutableLiveData<ProductFilterAttributes>? = null
    val sessionExpire: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        productListingData = MutableLiveData()
        filtersAttributesData = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun getProductListingFilterResponse(categoryId: String, hit: Boolean): MutableLiveData<ProductsByCategoryIdOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()

            val call = service.getProductsByCategoryId("category_id",
                "4",
                "eq",
                "visibility",
                "4",
                "eq",
                "created_at",
                "DESC",
                "10",
                "1"
            )

            call.enqueue(object : retrofit2.Callback<ProductsByCategoryIdOut> {
                override fun onFailure(call: retrofit2.Call<ProductsByCategoryIdOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    productListingData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ProductsByCategoryIdOut>,
                    response: retrofit2.Response<ProductsByCategoryIdOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            productListingData!!.value = response.body()
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
                            productListingData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            productListingData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return productListingData!!
    }

    fun getFilterAttributesResponse(filterAttributesIn: JsonObject?, hit: Boolean): MutableLiveData<ProductFilterAttributes> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()

            val call = service.getFiltersAttributes(filterAttributesIn)

            call.enqueue(object : retrofit2.Callback<ProductFilterAttributes> {
                override fun onFailure(call: retrofit2.Call<ProductFilterAttributes>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    filtersAttributesData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ProductFilterAttributes>,
                    response: retrofit2.Response<ProductFilterAttributes>
                ) {
                    when (response.code()) {
                        200 -> {
                            filtersAttributesData!!.value = response.body()
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
                            filtersAttributesData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            filtersAttributesData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return filtersAttributesData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }
}