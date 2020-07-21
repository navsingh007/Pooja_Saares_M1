package com.seasia.poojasarees.viewmodel.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.wishlist.AllWishlistProductsOut
import com.seasia.poojasarees.repository.wishlist.WishlistRepo
import com.seasia.poojasarees.utils.PreferenceKeys

class WishlistVM: ViewModel() {
    private var wishlistRepo: WishlistRepo
    private var addToWishlistData: MutableLiveData<Boolean>
    private var deleteFromWishlistData: MutableLiveData<Boolean>
    private var allWishlistProductsData: MutableLiveData<ArrayList<AllWishlistProductsOut>>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        wishlistRepo = WishlistRepo()
        addToWishlistData = MutableLiveData<Boolean>()
        deleteFromWishlistData = MutableLiveData<Boolean>()
        allWishlistProductsData = MutableLiveData<ArrayList<AllWishlistProductsOut>>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        addToWishlistData = wishlistRepo.addToWishlistResponse(null, false)
        deleteFromWishlistData = wishlistRepo.deleteFromWishlistResponse(null, false)
        allWishlistProductsData = wishlistRepo.allWishlistProductsResponse(null, false)
    }

    fun addToWishlist(productId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            val customerId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""

            val addToWishListIn = JsonObject()
            addToWishListIn.addProperty("customerId", customerId)
            addToWishListIn.addProperty("productId", productId)

            addToWishlistData = wishlistRepo.addToWishlistResponse(addToWishListIn, true)
            isLoading.postValue(true)
        }
    }

    fun deleteFromWishlist(productId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            val customerId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""

            val deleteFromWishListIn = JsonObject()
            deleteFromWishListIn.addProperty("customerId", customerId)
            deleteFromWishListIn.addProperty("productId", productId)

            deleteFromWishlistData = wishlistRepo.deleteFromWishlistResponse(deleteFromWishListIn, true)
            isLoading.postValue(true)
        }
    }

    fun allProductsInWishlist() {
        if (UtilsFunctions.isNetworkConnected()) {
            val customerId = MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_ID, "") ?: ""

            val allProductsInWishListIn = JsonObject()
            allProductsInWishListIn.addProperty("customerId", customerId)

            allWishlistProductsData = wishlistRepo.allWishlistProductsResponse(allProductsInWishListIn, true)
            isLoading.postValue(true)
        }
    }

    fun isAddedToWishlist(): LiveData<Boolean> {
        return addToWishlistData
    }

    fun isDeletedFromWishlist(): LiveData<Boolean> {
        return deleteFromWishlistData
    }

    fun allWishlistProducts(): LiveData<ArrayList<AllWishlistProductsOut>> {
        return allWishlistProductsData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return wishlistRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return wishlistRepo.isSessionExpire()
    }
}