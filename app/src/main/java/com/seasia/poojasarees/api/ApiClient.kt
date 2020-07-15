package com.seasia.poojasarees.api


import android.text.TextUtils
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.utils.PreferenceKeys
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
//    private const val BASE_URL = "http://stgm.appsndevs.com/pooja/rest/V1/"   // Milestone - 1

    private const val BASE_URL = "http://stgm.appsndevs.com/poojadev/rest/V1/"   // milestone-2

    const val BASE_URL_IMAGE = "http://stgm.appsndevs.com/poojadev/"   // Image base URL

    private var mApiInterface: ApiInterface? = null

    fun getApiInterface(): ApiInterface {
        return setApiInterface()!!
    }

    private fun setApiInterface(): ApiInterface? {
        var mAuthToken = ""
        val httpClient = OkHttpClient.Builder()
        val mBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        //        GlobalConstants.NOTIFICATION_TOKEN
        //  mAuthToken = GlobalConstants.NOTIFICATION_TOKEN;


//        val sharedPrefClass = SharedPrefClass()
//
//        if (sharedPrefClass.getPrefValue(
//                MyApplication.getInstance(),
//                GlobalConstants.SHARED_PREF,
//                GlobalConstants.SESSION_TOKEN
//            ) != null
//        ) {
//            mAuthToken = sharedPrefClass.getPrefValue(
//                MyApplication.instance,
//                GlobalConstants.SHARED_PREF,
//                GlobalConstants.SESSION_TOKEN
//            )
//        }


        //   mAuthToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InByYXRpYndoYUB5b3BtYWlsLmNvbSIsImlhdCI6MTU2MzM2ODMyMCwiZXhwIjoxNTYzNTQxMTIwfQ.gV5cZyj7Qgsn9BZVAayIj_LywgTn6InuPq6zGaJDvb4";


        if (PreferenceKeys.TOKEN == "0") {
            mAuthToken = PreferenceKeys.ADMIN_TOKEN
        } else {
            mAuthToken =
                "Bearer ${MyApplication.sharedPref.getString(PreferenceKeys.CUSTOMER_TOKEN, "")}"
//            mAuthToken = "Bearer ntkwiam9a6pm98puu22cjnjwnibnkt02";
        }


        //Print Api Logs
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.interceptors().add(logging)

        if (!TextUtils.isEmpty(mAuthToken)) {
            val finalMAuthToken = mAuthToken
            val interceptor = Interceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().header("Authorization", finalMAuthToken)
                val request = builder.build()
                chain.proceed(request)
            }
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor)
                mBuilder.client(httpClient.build())
                mApiInterface = mBuilder.build().create(ApiInterface::class.java)
            }
        } else
            mApiInterface = mBuilder.build().create(ApiInterface::class.java)

        return mApiInterface
    }
}// SharedPrefsHelper sharedPrefClass = new SharedPrefsHelper();
