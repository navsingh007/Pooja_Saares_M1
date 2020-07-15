package com.seasia.poojasarees.repository.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.seasia.poojasarees.R
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.request.UpdateProfileIn
import com.seasia.poojasarees.model.response.AllStatesOut
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.ProfileOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepo {
    var updateProfileData: MutableLiveData<ProfileOut>? = null
    var getProfileData: MutableLiveData<ProfileOut>? = null
    var allStatesData: MutableLiveData<ArrayList<AllStatesOut>>? = null
    var allTowns: MutableLiveData<ArrayList<AllTownsOut>>? = null
    val sessionExpire: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        getProfileData = MutableLiveData()
        updateProfileData = MutableLiveData()
        allStatesData = MutableLiveData()
        allTowns = MutableLiveData()
        sessionExpire = MutableLiveData()
        apiMsg = MutableLiveData()
        sessionExpire.postValue(false)
    }

    fun updateProfileResponse(userId: String, updateProfileIn: UpdateProfileIn?, hit: Boolean): MutableLiveData<ProfileOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.updateProfile(userId, updateProfileIn)
            call.enqueue(object : retrofit2.Callback<ProfileOut> {
                override fun onFailure(call: retrofit2.Call<ProfileOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    updateProfileData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ProfileOut>,
                    response: retrofit2.Response<ProfileOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            updateProfileData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)
//                                UtilsFunctions.showToastError(msg)
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                            }
                            updateProfileData!!.value = null

                        }
                        else -> {
                            updateProfileData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return updateProfileData!!
    }

    fun getProfileResponse(hit: Boolean): MutableLiveData<ProfileOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "1"
            val service = ApiClient.getApiInterface()
            val call = service.profileMe
            call.enqueue(object : retrofit2.Callback<ProfileOut> {
                override fun onFailure(call: retrofit2.Call<ProfileOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    getProfileData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<ProfileOut>,
                    response: retrofit2.Response<ProfileOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            getProfileData!!.value = response.body()
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
                            getProfileData!!.value = null

                        }
                        401 -> {
                            // Only for Customer token
//                            UtilsFunctions.showToastError(MyApplication.instance.resources.getString(
//                                R.string.session_expire))
                            apiMsg.postValue("SESSION_EXPIRED")
                            sessionExpire.postValue(true)
                        }
                        else -> {
                            getProfileData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return getProfileData!!
    }

    fun isSessionExpire(): MutableLiveData<Boolean> {
        return sessionExpire
    }

    fun allStatesResponse(hit: Boolean): MutableLiveData<ArrayList<AllStatesOut>> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getAllStates()
            call.enqueue(object : retrofit2.Callback<ArrayList<AllStatesOut>> {
                override fun onFailure(call: retrofit2.Call<ArrayList<AllStatesOut>>, t: Throwable) {
                    apiMsg.postValue(t.message!!)
//                    UtilsFunctions.showToastError(t.message!!)
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

//                                UtilsFunctions.showToastError(
//                                    jObjError.getString("message")
//                                )
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                            }
                            allStatesData!!.value = null
                        }
                        else -> {
                            allStatesData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
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

                                /*                               if (msg.equals("User with this phone already exist.")) {
                                                                   UtilsFunctions.showToastError(
                                                                       MyApplication.instance.resources.getString(
                                                                           R.string.register_phone_already_exist
                                                                       )
                                                                   )
                                                               } else {
                                                                   UtilsFunctions.showToastError(msg)
                                                               }*/
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

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }
}