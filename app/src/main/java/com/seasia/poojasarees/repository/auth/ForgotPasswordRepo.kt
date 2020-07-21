package com.seasia.poojasarees.repository.auth

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.response.authentication.SignupPhoneNoOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordRepo {
    var isValidPhoneData: MutableLiveData<SignupPhoneNoOut>? = null
    val apiMsg: MutableLiveData<String>

    init {
        isValidPhoneData = MutableLiveData()
        apiMsg = MutableLiveData()
    }

    fun phoneNoCheck(customerIn: JsonObject?, hit: Boolean): MutableLiveData<SignupPhoneNoOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.uniquePhoneNoCheck(customerIn)
            call.enqueue(object : Callback<SignupPhoneNoOut> {
                override fun onFailure(call: Call<SignupPhoneNoOut>, t: Throwable) {
                    apiMsg.postValue(t.message ?: "")
//                    UtilsFunctions.showToastError(t.message!!)
                    isValidPhoneData!!.value = null
                }

                override fun onResponse(
                    call: Call<SignupPhoneNoOut>,
                    response: Response<SignupPhoneNoOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            isValidPhoneData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())

                                val msg = jObjError.getString("message")

//                                apiMsg.postValue(msg)
                                if (!msg.equals("User with this phone already exist.")) {
                                    apiMsg.postValue(msg)
                                }

//                                if (!msg.equals("User with this phone already exist.")) {
//                                    UtilsFunctions.showToastError(MyApplication.instance.resources.getString(
//                                        R.string.register_phone_already_exist))
//                                }
                            } catch (e: Exception) {
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                                apiMsg.postValue(e.message ?: "An error occured")
                            }
                            isValidPhoneData!!.value = null

                        }
                        else -> {
                            isValidPhoneData!!.value = null
//                            UtilsFunctions.showToastError("Something went wrong")
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return isValidPhoneData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }
}