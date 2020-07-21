package com.seasia.poojasarees.views.products

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityProductDetailsBinding
import com.seasia.poojasarees.viewmodel.products.ProductDetailsVM

class ProductDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productDtlVM: ProductDetailsVM

    override fun getLayoutId(): Int {
        return R.layout.activity_product_details
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityProductDetailsBinding
        productDtlVM = ViewModelProvider(this).get(ProductDetailsVM::class.java)

        // Hardcoded
        productDtlVM.productDetailsById("35")
        productDetailsObserver()
        loadingObserver()
        showApiMsgObserver()
        showUserWarningObserver()
    }

    private fun productDetailsObserver() {
        productDtlVM.getProductDetails().observe(this, Observer { productDtl ->
            stopProgressDialog()

            if (productDtl != null) {
                UtilsFunctions.showToastSuccess("Toral items - ${productDtl.total_count}")
            }
        })
    }

    private fun loadingObserver() {
        productDtlVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }

    private fun showApiMsgObserver() {
        productDtlVM.showApiMsg().observe(this, Observer { msg ->
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
        productDtlVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                // TODO: 17-07-2020
            }
        })
    }
}