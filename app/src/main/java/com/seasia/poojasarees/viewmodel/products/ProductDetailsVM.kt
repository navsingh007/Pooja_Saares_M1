package com.seasia.poojasarees.viewmodel.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.products.ProductDetailsOut
import com.seasia.poojasarees.repository.products.ProductDetailsRepo

class ProductDetailsVM: ViewModel() {
    private var productDetailsRepo: ProductDetailsRepo
    private var productDetailsData: MutableLiveData<ProductDetailsOut>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        productDetailsRepo = ProductDetailsRepo()
        productDetailsData = MutableLiveData<ProductDetailsOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()
        productDetailsData = productDetailsRepo.getProductDetailsResponse("",false)
    }

    fun productDetailsById(productDetailById: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            productDetailsData = productDetailsRepo.getProductDetailsResponse(productDetailById, true)
            isLoading.postValue(true)
        }
    }

    fun getProductDetails(): LiveData<ProductDetailsOut> {
        return productDetailsData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return productDetailsRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return productDetailsRepo.isSessionExpire()
    }
}