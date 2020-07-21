package com.seasia.poojasarees.views.products

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityProductListingFilterBinding
import com.seasia.poojasarees.viewmodel.products.ProductListingVM

class ProductListingFilterActivity : BaseActivity() {
    private lateinit var binding: ActivityProductListingFilterBinding
    private lateinit var productListingVM: ProductListingVM

    override fun getLayoutId(): Int {
        return R.layout.activity_product_listing_filter
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityProductListingFilterBinding
        productListingVM = ViewModelProvider(this).get(ProductListingVM::class.java)

        /**
         * HARDCODED VALUE
         */
        productListingVM.filterProducts("35")
        productListingVM.initFilterAttributes("4")

        filteredProductsObserver()
        filterAttributesObserver()
        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()
    }

    private fun filteredProductsObserver() {
        productListingVM.getFilteredProducts().observe(this, Observer { filteredProducts ->
            stopProgressDialog()

            if (filteredProducts != null) {
                UtilsFunctions.showToastSuccess("Toral items - ${filteredProducts.total_count}")
            }
        })
    }

    private fun filterAttributesObserver() {
        productListingVM.getFilterAttributes().observe(this, Observer { filterAttributes ->
            stopProgressDialog()

            if (filterAttributes != null) {

            }
        })
    }

    private fun loadingObserver() {
        productListingVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        productListingVM.showApiMsg().observe(this, Observer { msg ->
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
        productListingVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                // TODO: 17-07-2020
            }
        })
    }
}