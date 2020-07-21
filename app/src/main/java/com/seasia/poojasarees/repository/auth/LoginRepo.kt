package com.seasia.poojasarees.repository.auth

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.model.response.authentication.LoginOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepo {
    var loginData: MutableLiveData<LoginOut>? = null
    val serverError: MutableLiveData<Boolean>
    val apiMsg: MutableLiveData<String>

    init {
        loginData = MutableLiveData()
        serverError = MutableLiveData()
        apiMsg = MutableLiveData()
    }

    fun loginResponse(loginIn: JsonObject?, hit: Boolean): MutableLiveData<LoginOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.loginUser(loginIn)
            call.enqueue(object : Callback<LoginOut> {
                override fun onFailure(call: Call<LoginOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message!!)
                    loginData!!.value = null
                }

                override fun onResponse(
                    call: Call<LoginOut>,
                    response: Response<LoginOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            loginData!!.value = response.body()
                        }
                        400, 404 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())

                                val msg = jObjError.getString("message")
                                apiMsg.postValue(msg)

/*                                if (msg.equals("Phone number is not registered")) {
                                    UtilsFunctions.showToastError(
                                        MyApplication.instance.resources.getString(
                                            R.string.login_phone_not_registered
                                        )
                                    )
                                } else if (msg.equals("Invalid phone or password.")) {
                                    UtilsFunctions.showToastError(
                                        MyApplication.instance.resources.getString(
                                            R.string.login_invalid_mobile_or_pass
                                        )
                                    )
                                } else {
                                    UtilsFunctions.showToastError(msg)
                                }*/
                            } catch (e: Exception) {
//                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                                apiMsg.postValue(e.message ?: "An error occured")
                            }
                            loginData!!.value = null

                        }
                        500 -> {
                            serverError.postValue(true)
                        }
                        else -> {
                            loginData!!.value = null
                            apiMsg.postValue("Something went wrong")
//                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return loginData!!
    }

    fun isServerError(): MutableLiveData<Boolean> {
        return serverError
    }

    fun showApiMsg(): MutableLiveData<String> {
        return apiMsg
    }
}