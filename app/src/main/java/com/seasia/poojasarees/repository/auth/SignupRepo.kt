package com.seasia.poojasarees.repository.auth

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.seasia.poojasarees.R
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.request.SignUpIn
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.model.response.SignUpOut
import com.seasia.poojasarees.model.response.SignupPhoneNoOut
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupRepo {
    var signupData: MutableLiveData<SignUpOut>? = null
    var isValidPhoneData: MutableLiveData<SignupPhoneNoOut>? = null
    var allTowns: MutableLiveData<ArrayList<AllTownsOut>>? = null
    val apiMsg: MutableLiveData<String>

    init {
        signupData = MutableLiveData()
        isValidPhoneData = MutableLiveData()
        allTowns = MutableLiveData()
        apiMsg = MutableLiveData()
    }

    fun signupResponse(customerIn: SignUpIn?, hit: Boolean): MutableLiveData<SignUpOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.signUpUser(customerIn)
            call.enqueue(object : Callback<SignUpOut> {
                override fun onFailure(call: Call<SignUpOut>, t: Throwable) {
                    apiMsg.postValue(t.message ?: "")
//                    UtilsFunctions.showToastError(t.message!!)
                    signupData!!.value = null
                }

                override fun onResponse(
                    call: Call<SignUpOut>,
                    response: Response<SignUpOut>
                ) {
                    when (response.code()) {
                        200 -> {
                            signupData!!.value = response.body()
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
                            signupData!!.value = null

                        }
                        else -> {
                            signupData!!.value = null
//                            UtilsFunctions.showToastError("Something went wrong")
                            apiMsg.postValue("Something went wrong")
                        }
                    }
                }
            })
        }
        return signupData!!
    }

    fun phoneNoCheck(customerIn: JsonObject?, hit: Boolean): MutableLiveData<SignupPhoneNoOut> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.uniquePhoneNoCheck(customerIn)
            call.enqueue(object : Callback<SignupPhoneNoOut> {
                override fun onFailure(call: Call<SignupPhoneNoOut>, t: Throwable) {
//                    UtilsFunctions.showToastError(t.message!!)
                    apiMsg.postValue(t.message ?: "")
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