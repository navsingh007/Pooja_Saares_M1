package com.seasia.poojasarees.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.request.AdminIn
import com.seasia.poojasarees.utils.PreferenceKeys
import org.json.JSONObject


class AdminTokenRepo {
    var adminTokenData: MutableLiveData<String>? = null

    init {
        adminTokenData = MutableLiveData()
    }

    fun adminTokenResponse(adminIn: AdminIn, hit: Boolean): MutableLiveData<String> {
        if (hit) {
            PreferenceKeys.TOKEN = "0"
            val service = ApiClient.getApiInterface()
            val call = service.getAdminToken(adminIn)
            call.enqueue(object : retrofit2.Callback<String> {
                override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                    UtilsFunctions.showToastError(t.message!!)
                    adminTokenData!!.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<String>,
                    response: retrofit2.Response<String>
                ) {
                    when (response.code()) {
                        200 -> {
                            adminTokenData!!.value = response.body()
                        }
                        401 -> {
                            try {
                                val jObjError =
                                    JSONObject(response.errorBody()!!.string())
                                UtilsFunctions.showToastError(
                                    jObjError.getString("message")
                                )
                            } catch (e: Exception) {
                                UtilsFunctions.showToastError(e.message ?: "An error occured")
                            }
                            adminTokenData!!.value = null

                        }
                        else -> {
                            adminTokenData!!.value = null
                            UtilsFunctions.showToastError("Something went wrong")
                        }
                    }
                }
            })
        }
        return adminTokenData!!
    }
}