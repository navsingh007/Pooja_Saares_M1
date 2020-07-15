package com.seasia.poojasarees.views.cms

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityWebviewBinding
import com.seasia.poojasarees.utils.PreferenceKeys


class PrivacyPolicyActivity : BaseActivity() {
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
            resources.getString(R.string.privacy_policy)
        binding.rlToolbar.tvCommonHeading.setTextColor(
            resources.getColor(R.color.colorWhite)
        )
    }

    private fun loadUrl() {
        binding.webviewGeneric.settings.setJavaScriptEnabled(true)

        val privacyPolicy =
            MyApplication.sharedPref.getString(PreferenceKeys.PRIVACY_POLICY, "") ?: ""
        var url = ""
        if (privacyPolicy.isEmpty()) {
            url = "file:///android_asset/comingsoon.html"
        } else {
            url = privacyPolicy
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
    }
}