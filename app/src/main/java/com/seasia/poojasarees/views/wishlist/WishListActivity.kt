package com.seasia.poojasarees.views.wishlist

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityWishlistListingBinding
import com.seasia.poojasarees.viewmodel.wishlist.WishlistVM

class WishListActivity : BaseActivity() {
    private lateinit var binding: ActivityWishlistListingBinding
    private lateinit var wishlistVM: WishlistVM

    override fun getLayoutId(): Int {
        return R.layout.activity_wishlist_listing
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityWishlistListingBinding
        wishlistVM = ViewModelProvider(this).get(WishlistVM::class.java)

        addedToWishlistObserver()
        removedFromWishlistObserver()
        allWishlistItemsObserver()
        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()

        // Temporary code
        wishlistVM.addToWishlist("35")
//        wishlistVM.deleteFromWishlist("35")
        wishlistVM.allProductsInWishlist()
    }

    private fun addedToWishlistObserver() {
        wishlistVM.isAddedToWishlist().observe(this, Observer { isAddedToWishlist ->
            stopProgressDialog()

            if (isAddedToWishlist != null) {
                if (isAddedToWishlist) {

                } else {

                }
            }
        })
    }

    private fun removedFromWishlistObserver() {
        wishlistVM.isDeletedFromWishlist().observe(this, Observer { isDeletedFromWishlist ->
            stopProgressDialog()

            if (isDeletedFromWishlist != null) {
                if (isDeletedFromWishlist) {

                } else {

                }
            }
        })
    }

    private fun allWishlistItemsObserver() {
        wishlistVM.allWishlistProducts().observe(this, Observer { allWishlistItems ->
            stopProgressDialog()

            if (allWishlistItems != null) {

            }
        })
    }

    private fun loadingObserver() {
        wishlistVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        wishlistVM.showApiMsg().observe(this, Observer { msg ->
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
        wishlistVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                // TODO: 17-07-2020
            }
        })
    }
}