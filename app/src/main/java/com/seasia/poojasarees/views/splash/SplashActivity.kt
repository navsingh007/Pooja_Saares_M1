package com.seasia.poojasarees.views.splash

import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivitySplashBinding
import com.seasia.poojasarees.fcm.FcmUtils
import com.seasia.poojasarees.utils.LocaleManager
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.views.auth.LoginActivity
import com.seasia.poojasarees.views.home.HomeActivity
import java.util.*

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val intanceId = FcmUtils.getInstanceId()
        Log.d("Splash", "ID - $intanceId")

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    checkScreenType()
                }
            }
        }, 3000)

        // Locale set on activity launch(workaround) - Below API 24
        // Need - Not launching LoginActivity in selected language
        setLocaleOnActivityLaunch()
    }

    private fun setLocaleOnActivityLaunch() {
        if (Build.VERSION.SDK_INT < 24) {
            when (LocaleManager.getLanguagePref(this)) {
                LocaleManager.HINDI -> {
                    LocaleManager.setNewLocale(this, LocaleManager.HINDI)
                }
                LocaleManager.ENGLISH -> {
                    LocaleManager.setNewLocale(this, LocaleManager.ENGLISH)
                }
            }
        }
    }

    private fun checkScreenType() {
        PreferenceKeys.TOKEN = "0"
//        val loginToken = MyApplication.sharedPref.getString(PreferenceKeys.LOGIN_TOKEN1, "")
//        val storeId = MyApplication.sharedPref.getString(PreferenceKeys.STORE_ID, "")

        val isLogin = MyApplication.sharedPref.getBoolean(PreferenceKeys.IS_LOGIN)

        val intent: Intent
        if (isLogin) {
            intent = Intent(this, HomeActivity::class.java)
        } else {
            intent = Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
