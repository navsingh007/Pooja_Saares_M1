package com.seasia.poojasarees.application

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.seasia.poojasarees.R
import com.seasia.poojasarees.utils.FontStyle
import com.seasia.poojasarees.utils.LocaleManager
import com.seasia.poojasarees.utils.PrefStore
import java.util.*


class MyApplication : MultiDexApplication() {
    private var customFontFamily: FontStyle? = null

    override fun onCreate() {
        MultiDex.install(this)
        super.onCreate()

        instance = this
        gson = GsonBuilder().serializeNulls().create()
        sharedPref = PrefStore(this)

        MultiDex.install(this)
        customFontFamily = FontStyle.getInstance()
//        customFontFamily!!.addFont("regular", "Montserrat-Regular_0.ttf")
//        customFontFamily!!.addFont("semibold", "Montserrat-Medium_0.ttf")
//        customFontFamily!!.addFont("bold", "Montserrat-SemiBold_0.ttf")
    }

    // locale methods
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.setLocale(this)
    }


    companion object {
        /**
         * @return ApplicationController singleton instance
         */
        @get:Synchronized
        lateinit var instance: MyApplication
        lateinit var gson: Gson
        lateinit var sharedPref: PrefStore
    }
}