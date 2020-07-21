package com.seasia.poojasarees.repository.wishlist

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.response.wishlist.AllWishlistProductsOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject

class WishlistRepo {
    private var addToWishlistData: MutableLiveData<Boolean>? = null
    private var deleteFromWishlistData: MutableLiveData<Boolean>? = null
    private var allWishlistProductsData: MutableLiveData<ArrayList<AllWishlistProductsOut>>? = null
    private val sessionExpire: MutableLiveData<Boolean>
    private val apiMsg: MutableLiveData<String>

    init {
        addToWishlistData = MutableLiveData()
        deleteFromWishlistData = MutableLiveData()
        allWishlistProductsData = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun addToWishlistResponse(
        addToWishlistIn: JsonObject?,
        hit: Boolean
    ): MutableLiveData<Boolean> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.addProductToWishlist(addToWishlistIn)

            call.enqueue(object : retrofit2.Callback<Boolean> {
                override fun onFailure(call: retrofit2.Call<Boolean>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    addToWishlistData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<Boolean>,
                    response: retrofit2.Response<Boolean>
                ) {
                    when (response.code()) {
                        200 -> {
                            addToWishlistData!!.value = response.body()
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
                            addToWishlistData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            addToWishlistData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return addToWishlistData!!
    }

    fun deleteFromWishlistResponse(
        deleteFromWishlistIn: JsonObject?,
        hit: Boolean
    ): MutableLiveData<Boolean> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.deleteProductFromWishlist(deleteFromWishlistIn)

            call.enqueue(object : retrofit2.Callback<Boolean> {
                override fun onFailure(call: retrofit2.Call<Boolean>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    deleteFromWishlistData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<Boolean>,
                    response: retrofit2.Response<Boolean>
                ) {
                    when (response.code()) {
                        200 -> {
                            deleteFromWishlistData!!.value = response.body()
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
                            deleteFromWishlistData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            deleteFromWishlistData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return deleteFromWishlistData!!
    }

    fun allWishlistProductsResponse(
        allWishlistProductsIn: JsonObject?,
        hit: Boolean
    ): MutableLiveData<ArrayList<AllWishlistProductsOut>> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getWishlistItems(allWishlistProductsIn)

            call.enqueue(object : retrofit2.Callback<ArrayList<AllWishlistProductsOut>> {
                override fun onFailure(call: retrofit2.Call<ArrayList<AllWishlistProductsOut>>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    allWishlistProductsData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ArrayList<AllWishlistProductsOut>>,
                    response: retrofit2.Response<ArrayList<AllWishlistProductsOut>>
                ) {
                    when (response.code()) {
                        200 -> {
                            allWishlistProductsData!!.value = response.body()
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
                            allWishlistProductsData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            allWishlistProductsData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return allWishlistProductsData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }
}