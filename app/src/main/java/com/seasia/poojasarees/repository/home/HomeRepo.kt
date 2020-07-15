package com.seasia.poojasarees.repository.home

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.HomeOut
import com.seasia.poojasarees.model.response.ProfilePicOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepo {
    var homeData: MutableLiveData<HomeOut>? = null
    var profilePicData: MutableLiveData<ProfilePicOut>? = null
    val serverError: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        homeData = MutableLiveData()
        profilePicData = MutableLiveData()
        serverError = MutableLiveData()
        apiMsg = MutableLiveData()
    }

    fun homeResponse(homeIn: JsonObject?, hit: Boolean): MutableLiveData<HomeOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getHomeContent(homeIn)
            call.enqueue(object : Callback<HomeOut> {
                override fun onFailure(call: Call<HomeOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)

                    apiMsg.postValue(t.message!!)
                    homeData!!.value = null
                }

                override fun onResponse(
                    call: Call<HomeOut>,
                    response: Response<HomeOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            homeData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)

//                                UtilsFunctions.showToastError(
//                                    jObjError.getString("message")
//                                )
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                            }
                            homeData!!.value = null

                        }
                        500 -> {
                            serverError.postValue(true)
                        }
                        else -> {
                            homeData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return homeData!!
    }

    fun isServerError(): MutableLiveData<Boolean> {
        return serverError
    }

    fun profilePicResponse(profilePicIn: JsonObject?, hit: Boolean): MutableLiveData<ProfilePicOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.updateProfilePic(profilePicIn)
            call.enqueue(object : Callback<ProfilePicOut> {
                override fun onFailure(call: Call<ProfilePicOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    profilePicData!!.value = null
                }

                override fun onResponse(
                    call: Call<ProfilePicOut>,
                    response: Response<ProfilePicOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            profilePicData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)
//                                UtilsFunctions.showToastError(
//                                    jObjError.getString("message")
//                                )
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                            }
                            profilePicData!!.value = null

                        }
                        else -> {
                            profilePicData!!.value = null
//                            UtilsFunctions.showToastError("Something went wrong")
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return profilePicData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }
}