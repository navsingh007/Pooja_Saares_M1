package com.seasia.poojasarees.repository.address

import androidx.lifecycle.MutableLiveData
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.request.AddressIn
import com.seasia.poojasarees.model.response.AddressOut
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject

class AddressRepo {

    var addOrUpdateAddressData: MutableLiveData<AddressOut>? = null
    var allStatesData: MutableLiveData<ArrayList<AllStatesOut>>? = null
    val sessionExpire: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        addOrUpdateAddressData = MutableLiveData()
        allStatesData = MutableLiveData()
        sessionExpire = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun addOrUpdateAddress(userId: String, updateAddressIn: AddressIn?, hit: Boolean): MutableLiveData<AddressOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.addOrUpdateAddress(userId, updateAddressIn)
            call.enqueue(object : retrofit2.Callback<AddressOut> {
                override fun onFailure(call: retrofit2.Call<AddressOut>, t: Throwable) {
                    apiMsg.postValue(t.message!!)
                    addOrUpdateAddressData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<AddressOut>,
                    response: retrofit2.Response<AddressOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            addOrUpdateAddressData!!.value = response.body()
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
                            addOrUpdateAddressData!!.value = null

                        }
                        else -> {
                            addOrUpdateAddressData!!.value = null
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return addOrUpdateAddressData!!
    }

    fun allStatesResponse(hit: Boolean): MutableLiveData<ArrayList<AllStatesOut>> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getAllStates()
            call.enqueue(object : retrofit2.Callback<ArrayList<AllStatesOut>> {
                override fun onFailure(call: retrofit2.Call<ArrayList<AllStatesOut>>, t: Throwable) {
                    apiMsg.postValue(t.message!!)
                    allStatesData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ArrayList<AllStatesOut>>,
                    response: retrofit2.Response<ArrayList<AllStatesOut>>
                ) {
                    when (response.code()) {
                        200 -> {
                            allStatesData!!.value = response.body()
                        }
                        401 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
                            }
                            allStatesData!!.value = null
                        }
                        else -> {
                            allStatesData!!.value = null
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return allStatesData!!
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }
}