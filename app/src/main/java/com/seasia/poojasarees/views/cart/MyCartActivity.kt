package com.seasia.poojasarees.views.cart

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityCartListingBinding
import com.seasia.poojasarees.viewmodel.cart.CartVM

class MyCartActivity: BaseActivity() {
    private lateinit var binding: ActivityCartListingBinding
    private lateinit var cartVM: CartVM

    override fun getLayoutId(): Int {
        return R.layout.activity_cart_listing
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityCartListingBinding
        cartVM = ViewModelProvider(this).get(CartVM::class.java)

        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()

        // cart
        createCartObserver()
        addToCartObserver()
        allCartItemsObserver()
        deleteItemFromCartObserver()

        cartVM.createCart()
    }

    private fun createCartObserver() {
        cartVM.onCreateCart().observe(this, Observer { createCartId ->
            stopProgressDialog()

            if (createCartId != null) {
                cartVM.addToCart(createCartId.toString())
                cartVM.getCustByCartId(createCartId.toString())
            }
        })
    }

    private fun addToCartObserver() {
        cartVM.onAddToCart().observe(this, Observer { addToCart ->
            stopProgressDialog()

            if (addToCart != null) {

            }
        })
    }

    private fun allCartItemsObserver() {
        cartVM.getAllCartProducts().observe(this, Observer { allCartItems ->
            stopProgressDialog()

            if (allCartItems != null) {

            }
        })
    }

    private fun deleteItemFromCartObserver() {
        cartVM.deleteItemFromCart().observe(this, Observer { deleteCartItem ->
            stopProgressDialog()

            if (deleteCartItem != null) {
                if (deleteCartItem) {
                    // Item deleted from cart
                } else {
                    // Item not deleted from cart
                }
            }
        })
    }








    private fun loadingObserver() {
        cartVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        cartVM.showApiMsg().observe(this, Observer { msg ->
            stopProgressDialog()

            if (msg != null) {
                if (msg.equals("*****************************")) {
                    // TODO: 17-07-2020
                } else {
                    UtilsFunctions.showToastError(msg)
                }
            }
        })
    }

    private fun showUserWarningObserver() {
        cartVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                // TODO: 17-07-2020
            }
        })
    }
}