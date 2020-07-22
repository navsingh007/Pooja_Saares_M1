package com.seasia.poojasarees.repository.address

import androidx.lifecycle.MutableLiveData
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.AddressIn
import com.seasia.poojasarees.model.AddressOut
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.Addresses
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepo {

    var addOrUpdateAddressData: MutableLiveData<AddressOut>? = null
    var deleteAddressData: MutableLiveData<Boolean>? = null
    var getAddressByIdData: MutableLiveData<Addresses>? = null
    var allStatesData: MutableLiveData<ArrayList<AllStatesOut>>? = null
    var allTowns: MutableLiveData<ArrayList<AllTownsOut>>? = null
    val sessionExpire: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        addOrUpdateAddressData = MutableLiveData()
        deleteAddressData = MutableLiveData()
        getAddressByIdData = MutableLiveData()
        allStatesData = MutableLiveData()
        allTowns = MutableLiveData()
        sessionExpire = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun addOrUpdateAddress(
        userId: String,
        updateAddressIn: AddressIn?,
        hit: Boolean
    ): MutableLiveData<AddressOut> {
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

    fun deleteAddressById(
        addressId: String,
        hit: Boolean
    ): MutableLiveData<Boolean> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.deleteAddressById(addressId)
            call.enqueue(object : retrofit2.Callback<Boolean> {
                override fun onFailure(call: retrofit2.Call<Boolean>, t: Throwable) {
                    apiMsg.postValue(t.message!!)
                    deleteAddressData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<Boolean>,
                    response: retrofit2.Response<Boolean>
                ) {
                    when (response.code()) {
                        200 -> {
                            deleteAddressData!!.value = response.body()
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
                            deleteAddressData!!.value = null

                        }
                        else -> {
                            deleteAddressData!!.value = null
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return deleteAddressData!!
    }

    fun getAddressByID(
        addressId: String,
        hit: Boolean
    ): MutableLiveData<Addresses> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getAddressById(addressId)
            call.enqueue(object : retrofit2.Callback<Addresses> {
                override fun onFailure(call: retrofit2.Call<Addresses>, t: Throwable) {
                    apiMsg.postValue(t.message!!)
                    getAddressByIdData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<Addresses>,
                    response: retrofit2.Response<Addresses>
                ) {
                    when (response.code()) {
                        200 -> {
                            getAddressByIdData!!.value = response.body()
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
                            getAddressByIdData!!.value = null

                        }
                        else -> {
                            getAddressByIdData!!.value = null
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return getAddressByIdData!!
    }

    fun allStatesResponse(hit: Boolean): MutableLiveData<ArrayList<AllStatesOut>> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getAllStates()
            call.enqueue(object : retrofit2.Callback<ArrayList<AllStatesOut>> {
                override fun onFailure(
                    call: retrofit2.Call<ArrayList<AllStatesOut>>,
                    t: Throwable
                ) {
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

    fun getAllTowns(hit: Boolean): MutableLiveData<ArrayList<AllTownsOut>> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getAllTowns()
            call.enqueue(object : Callback<ArrayList<AllTownsOut>> {
                override fun onFailure(call: Call<ArrayList<AllTownsOut>>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message ?: "")
                    allTowns!!.value = null
                }

                override fun onResponse(
                    call: Call<ArrayList<AllTownsOut>>,
                    response: Response<ArrayList<AllTownsOut>>
                ) {
                    when (response.code()) {
                        200 -> {
                            allTowns!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())

                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)
                            } catch (e: Exception) {
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                                apiMsg.postValue(e.message ?: "An error occured")
                            }
                            allTowns!!.value = null

                        }
                        else -> {
                            allTowns!!.value = null
//                            UtilsFunctions.showToastError("Something went wrong")
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return allTowns!!
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }
}