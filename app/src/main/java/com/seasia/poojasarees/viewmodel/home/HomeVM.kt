package com.seasia.poojasarees.viewmodel.home

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.HomeOut
import com.seasia.poojasarees.model.response.ProfilePicOut
import com.seasia.poojasarees.repository.home.HomeRepo
import com.seasia.poojasarees.utils.PreferenceKeys
import java.io.File
import java.util.Base64.getEncoder

class HomeVM : ViewModel() {
    private var homeRepo: HomeRepo
    private var homeData: MutableLiveData<HomeOut>
    private var profilePicData: MutableLiveData<ProfilePicOut>
    private val isLoading: MutableLiveData<Boolean>

    init {
        homeRepo = HomeRepo()
        homeData = MutableLiveData<HomeOut>()
        profilePicData = MutableLiveData<ProfilePicOut>()
        isLoading = MutableLiveData<Boolean>()

        homeData = homeRepo.homeResponse(null, false)
        profilePicData = homeRepo.profilePicResponse(null, false)
    }

    fun getHomeDataAccToLang(languageId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            val loginIn = JsonObject()
            loginIn.addProperty("storeId", languageId)
            homeData = homeRepo.homeResponse(loginIn, true)
            isLoading.postValue(true)
        }
    }

    fun updateProfilePic() {
        if (UtilsFunctions.isNetworkConnected()) {
            val custId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID)
            val profilePicIn = JsonObject()

//        val data: ByteArray = profileImage.toByteArray()
//        val base64: String = Base64.encodeToString(data, Base64.DEFAULT)

            profilePicIn.addProperty("customerId", custId)
            profilePicIn.addProperty("content", encoder(profileImage))

            profilePicData = homeRepo.profilePicResponse(profilePicIn, true)
            isLoading.postValue(true)
        }
    }

    fun encoder(filePath: String): String {
        val bytes = File(filePath).readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
        return base64
    }

    fun isServerError(): LiveData<Boolean> {
        return homeRepo.isServerError()
    }

    fun homeResponse(): LiveData<HomeOut> {
        return homeData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getProfilePicResponse(): LiveData<ProfilePicOut> {
        return profilePicData
    }

    fun showApiMsg(): LiveData<String> {
        return homeRepo.showApiMsg()
    }


    companion object {
        var profileImage = ""
    }
}