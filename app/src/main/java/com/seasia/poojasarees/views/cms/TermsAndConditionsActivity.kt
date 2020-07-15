package com.seasia.poojasarees.views.cms

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityWebviewBinding
import com.seasia.poojasarees.utils.PreferenceKeys


class TermsAndConditionsActivity : BaseActivity() {
    private lateinit var binding: ActivityWebviewBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun initViews() {
//        binding = viewDataBinding as ActivityWebviewBinding

        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview)
        initToolbar()
        loadUrl()
    }

    private fun initToolbar() {
        binding.rlToolbar.tvCommonHeading.text =
            resources.getString(R.string.terms_and_conditions)
        binding.rlToolbar.tvCommonHeading.setTextColor(
            resources.getColor(R.color.colorWhite)
        )
    }

    private fun loadUrl() {
//        binding.webview.loadUrl("www.google.com")

        binding.webviewGeneric.settings.setJavaScriptEnabled(true)

//        val url = "https://www.cerebruminfotech.com"

        val termsAndConditions =
            MyApplication.sharedPref.getString(PreferenceKeys.TERMS_N_CONDITIONS, "") ?: ""
        var url = ""
        if (termsAndConditions.isEmpty()) {
            url = "file:///android_asset/comingsoon.html"
        } else {
            url = termsAndConditions
        }
        binding.webviewGeneric.loadUrl(url)

        binding.webviewGeneric.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(view.url)
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
                handler.proceed() // Ignore SSL certificate errors
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                startProgressDialog()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                stopProgressDialog()
            }
        })
//        binding.webviewGeneric.loadUrl("http://www.google.com")
    }


    /*   private fun loadUrl() {
           startProgressDialog()
   //        binding.webview.loadUrl("www.google.com")

           binding.webviewGeneric.getSettings().setLoadWithOverviewMode(true);
           binding.webviewGeneric.getSettings().setUseWideViewPort(true);
           binding.webviewGeneric.getSettings().setBuiltInZoomControls(true);
           binding.webviewGeneric.getSettings().setPluginState(WebSettings.PluginState.ON);
           binding.webviewGeneric.settings.setJavaScriptEnabled(true)
   //        binding.webview.loadUrl("http://www.google.com")

   //        val url = "https://www.cerebruminfotech.com"
           val url = "file:///android_asset/comingsoon.html"

   //        webView.loadUrl("http://www.teluguoneradio.com/rssHostDescr.php?hostId=147");
   //        webView.loadUrl("http://www.google.com");
   //        binding.webviewGeneric.loadUrl("https://www.google.com")
           binding.webviewGeneric.loadUrl(url)

           binding.webviewGeneric.setWebViewClient(object : WebViewClient() {
               override fun shouldOverrideUrlLoading(
                   view: WebView,
                   request: WebResourceRequest
               ): Boolean {
                   stopProgressDialog()
                   view.loadUrl(view.url)
                   return false
               }

               override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                   view.loadUrl(url)
                   stopProgressDialog()
                   return true
               }

               override fun onReceivedSslError(
                   view: WebView?,
                   handler: SslErrorHandler,
                   error: SslError?
               ) {
                   stopProgressDialog()
                   handler.proceed() // Ignore SSL certificate errors
               }
           })

           stopProgressDialog()
       }*/
}