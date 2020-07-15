package com.seasia.poojasarees.repository.auth

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.R
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.LoginOut
import com.seasia.poojasarees.model.response.OtpOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpRepo {
    var sendOtpData: MutableLiveData<OtpOut>? = null
    var verifyOtpData: MutableLiveData<OtpOut>? = null
    val apiMsg: MutableLiveData<String>

    init {
        sendOtpData = MutableLiveData()
        verifyOtpData = MutableLiveData()
        apiMsg = MutableLiveData()
    }

    fun sendOtpResponse(otpIn: JsonObject?, hit: Boolean): MutableLiveData<OtpOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.sendOtp(otpIn)
            call.enqueue(object : Callback<OtpOut> {
                override fun onFailure(call: Call<OtpOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    sendOtpData!!.value = null
                }

                override fun onResponse(
                    call: Call<OtpOut>,
                    response: Response<OtpOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            sendOtpData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                val msg = jObjError.getString("message")
//                                UtilsFunctions.showToastError(msg)

                                apiMsg.postValue(msg)
                            } catch (e: Exception) {
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                                apiMsg.postValue(e.message ?: "An error occured")
                            }
                            sendOtpData!!.value = null
                        }
                        else -> {
                            sendOtpData!!.value = null
//                            UtilsFunctions.showToastError("Something went wrong")
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return sendOtpData!!
    }

    fun verifyOtpResponse(otpIn: JsonObject?, hit: Boolean): MutableLiveData<OtpOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.verifyOtp(otpIn)
            call.enqueue(object : Callback<OtpOut> {
                override fun onFailure(call: Call<OtpOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)

                    apiMsg.postValue(t.message!!)
                    verifyOtpData!!.value = null
                }

                override fun onResponse(
                    call: Call<OtpOut>,
                    response: Response<OtpOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            verifyOtpData!!.value = response.body()
                        }
                        400 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())

                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)

/*                                if (msg.equals("Invalid OTP")) {
                                    UtilsFunctions.showToastError(
                                        MyApplication.instance.resources.getString(
                                            R.string.otp_invalid
                                        )
                                    )
                                } else {
                                    UtilsFunctions.showToastError(msg)
                                }*/
                            } catch (e: Exception) {
                                apiMsg.postValue(e.message ?: "An error occured")
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                            }
                            verifyOtpData!!.value = null

                        }
                        else -> {
                            verifyOtpData!!.value = null
//                            UtilsFunctions.showToastError("Something went wrong")
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return verifyOtpData!!
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }
}