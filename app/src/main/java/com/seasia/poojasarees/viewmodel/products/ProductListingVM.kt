package com.seasia.poojasarees.viewmodel.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.response.products.ProductFilterAttributes
import com.seasia.poojasarees.model.response.products.ProductsByCategoryIdOut
import com.seasia.poojasarees.repository.products.ProductListingRepo

class ProductListingVM : ViewModel() {
    private var productListingRepo: ProductListingRepo
    private var productsFilterData: MutableLiveData<ProductsByCategoryIdOut>
    private var filterAttributesData: MutableLiveData<ProductFilterAttributes>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        productListingRepo = ProductListingRepo()
        productsFilterData = MutableLiveData<ProductsByCategoryIdOut>()
        filterAttributesData = MutableLiveData<ProductFilterAttributes>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        productsFilterData = productListingRepo.getProductListingFilterResponse("", "", false)
        filterAttributesData = productListingRepo.getFilterAttributesResponse(null, false)
    }

    fun filterProducts(pageCount: String, productsByCatId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            productsFilterData =
                productListingRepo.getProductListingFilterResponse(pageCount, productsByCatId, true)
//            isLoading.postValue(true)
        }
    }

    fun initFilterAttributes(categoryId: String) {
        if (UtilsFunctions.isNetworkConnected()) {

            val filterAttributes = JsonObject()
            filterAttributes.addProperty(
                "store_id",
                UtilsFunctions.getLanguageStoreId(MyApplication.instance)
            )
            filterAttributes.addProperty("categoryId", categoryId)

            filterAttributesData =
                productListingRepo.getFilterAttributesResponse(filterAttributes, true)
            isLoading.postValue(true)
        }
    }

    fun getFilterAttributes(): LiveData<ProductFilterAttributes> {
        return filterAttributesData
    }

    fun getFilteredProducts(): LiveData<ProductsByCategoryIdOut> {
        return productsFilterData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return productListingRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return productListingRepo.isSessionExpire()
    }
}