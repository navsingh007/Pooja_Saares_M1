package com.seasia.poojasarees.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seasia.poojasarees.model.response.category.CategoryListOut
import com.seasia.poojasarees.repository.category.CategoryRepo

class CategoryVM : ViewModel() {
    private var categoryRepo: CategoryRepo
    private var getCategoryList: MutableLiveData<CategoryListOut>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        categoryRepo = CategoryRepo()
        getCategoryList = MutableLiveData<CategoryListOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        isLoading.postValue(true)
        getCategoryList = categoryRepo.getCategoryListResponse(true)
    }

    fun getCategories(): LiveData<CategoryListOut> {
        return getCategoryList
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return categoryRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return categoryRepo.isSessionExpire()
    }
}