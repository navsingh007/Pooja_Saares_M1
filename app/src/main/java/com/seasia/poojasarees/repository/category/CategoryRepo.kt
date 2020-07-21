package com.seasia.poojasarees.repository.category

import androidx.lifecycle.MutableLiveData
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.response.category.CategoryListOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject

class CategoryRepo {
    var getCategoryData: MutableLiveData<CategoryListOut>? = null
    val sessionExpire: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        getCategoryData = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun getCategoryListResponse(hit: Boolean): MutableLiveData<CategoryListOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.categoryListing
            call.enqueue(object : retrofit2.Callback<CategoryListOut> {
                override fun onFailure(call: retrofit2.Call<CategoryListOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    getCategoryData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<CategoryListOut>,
                    response: retrofit2.Response<CategoryListOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            getCategoryData!!.value = response.body()
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
                            getCategoryData!!.value = null

                        }
                        401 -> {
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            getCategoryData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return getCategoryData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }
}