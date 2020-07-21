package com.seasia.poojasarees.viewmodel.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.model.request.cart.AddToCartIn
import com.seasia.poojasarees.model.response.cart.AddToCartOut
import com.seasia.poojasarees.model.response.cart.CustomerByCartIdOut
import com.seasia.poojasarees.repository.cart.CartRepo
import com.seasia.poojasarees.utils.PreferenceKeys

class CartVM: ViewModel() {
    private var cartRepo: CartRepo
    private var createCartData: MutableLiveData<Int>
    private var addToCartData: MutableLiveData<AddToCartOut>
    private var getCustByCartIdData: MutableLiveData<CustomerByCartIdOut>
    private val isLoading: MutableLiveData<Boolean>
    private val userMsg: MutableLiveData<String>

    init {
        cartRepo = CartRepo()
        createCartData = MutableLiveData<Int>()
        addToCartData = MutableLiveData<AddToCartOut>()
        getCustByCartIdData = MutableLiveData<CustomerByCartIdOut>()
        isLoading = MutableLiveData<Boolean>()
        userMsg = MutableLiveData<String>()

        createCartData = cartRepo.createCartResponse(false)
        addToCartData = cartRepo.addToCartReponse(null, false)
        getCustByCartIdData = cartRepo.getCustomerByCartIdResponse(null, false)
    }

    fun createCart() {
        if (UtilsFunctions.isNetworkConnected()) {
            createCartData = cartRepo.createCartResponse(true)
            isLoading.postValue(true)
        }
    }

    fun addToCart(cartId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            val addToCartIn = AddToCartIn()
            addToCartIn.cartItem.sku = "banarasi Saree"
            addToCartIn.cartItem.qty = 1
            addToCartIn.cartItem.quote_id = cartId

            addToCartData = cartRepo.addToCartReponse(addToCartIn, true)
            isLoading.postValue(true)
        }
    }

    fun getCustByCartId(cartId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            getCustByCartIdData = cartRepo.getCustomerByCartIdResponse(cartId, true)
            isLoading.postValue(true)
        }
    }

    fun onCreateCart(): LiveData<Int> {
        return createCartData
    }

    fun onAddToCart(): LiveData<AddToCartOut> {
        return addToCartData
    }

    fun getAllCartProducts(): LiveData<CustomerByCartIdOut> {
        return getCustByCartIdData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun showApiMsg(): LiveData<String> {
        return cartRepo.showApiMsg()
    }

    fun showUserWarning(): LiveData<String> {
        return userMsg
    }

    fun isSessionExpire(): LiveData<Boolean> {
        return cartRepo.isSessionExpire()
    }
}