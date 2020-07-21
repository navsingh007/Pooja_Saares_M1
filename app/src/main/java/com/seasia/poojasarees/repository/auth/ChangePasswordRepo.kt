package com.seasia.poojasarees.repository.auth

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.response.authentication.OtpOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordRepo {
    var changePassData: MutableLiveData<OtpOut>? = null
    val apiMsg: MutableLiveData<String>

    init {
        changePassData = MutableLiveData()
        apiMsg = MutableLiveData()
    }

    fun changePassResponse(changePassIn: JsonObject?, hit: Boolean): MutableLiveData<OtpOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.changePassword(changePassIn)
            call.enqueue(object : Callback<OtpOut> {
                override fun onFailure(call: Call<OtpOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message ?: "")
                    changePassData!!.value = null
                }

                override fun onResponse(
                    call: Call<OtpOut>,
                    response: Response<OtpOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            changePassData!!.value = response.body()
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
                            changePassData!!.value = null

                        }
                        else -> {
                            changePassData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return changePassData!!
    }
}