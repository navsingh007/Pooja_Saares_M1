package com.seasia.poojasarees.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seasia.poojasarees.model.request.AdminIn
import com.seasia.poojasarees.repository.AdminTokenRepo

class AdminVM: ViewModel() {
    private var adminRepo: AdminTokenRepo
    private var adminData: MutableLiveData<String>

    init {
        adminRepo = AdminTokenRepo()
        adminData = MutableLiveData<String>()

        getAdminToken()
    }

    private fun getAdminToken() {
        val adminIn = AdminIn()
        adminData = adminRepo.adminTokenResponse(adminIn, true)
    }

    fun adminToken(): LiveData<String> {
        return adminData
    }
}