package com.seasia.poojasarees.views.products

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.text.Html
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityProductDetailsBinding
import com.seasia.poojasarees.viewmodel.products.ProductDetailsVM
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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

        handleIntent()
//        shareProduct()

        shareItem("Pooja Sarees ", "http://stgm.appsndevs.com/pooja/pub/media/catalog/product/i/m/image_6_1_1.jpg")
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

    /**
     *  Share product to another App
     */
    fun shareItem(productName: String, url: String?) {
        Picasso.get().load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "image/*"
                i.putExtra(Intent.EXTRA_TEXT,
                    Html.fromHtml("$productName - http://stgm.appsndevs.com/pooja/pub/media/catalog/product/i/m/image_6_1_1.jpg")
                        .toString()
                )
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap!!))
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(i, "Share Image"))
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })
    }

    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()

            // Old approach causes - CRASH
//            bmpUri = Uri.fromFile(file)

            // New approach
            bmpUri = FileProvider.getUriForFile(
                this, getApplicationContext()
                    .getPackageName().toString() + ".fileprovider", file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }


    private fun handleIntent() {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            Log.d("ProdDetails ", " ==========> $appLinkAction ${appLinkData.toString()}")
        }
    }
}