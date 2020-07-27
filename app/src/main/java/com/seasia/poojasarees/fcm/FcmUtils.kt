package com.seasia.poojasarees.fcm

import android.app.Activity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.utils.PreferenceKeys


object FcmUtils {
    private val TAG = "FcmUtils"
    val RC_PLAY_SERVICE_AVAILABLE = 2404

//    fun isGooglePlayServicesAvailable(context: Context?): Boolean {
//        val googleApiAvailability = GoogleApiAvailability.getInstance()
//        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
//        return resultCode == ConnectionResult.SUCCESS
//    }

    fun isGooglePlayServicesAvailable(activity: Activity?): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, RC_PLAY_SERVICE_AVAILABLE)
                    .show()
            }
            return false
        }
        return true
    }

    fun getInstanceId(): String {
        var token = ""
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token!!

                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)

                val msg = "FCM Token - $token"

                MyApplication.sharedPref.saveString(PreferenceKeys.FCM_DEVICE_TOKEN, token)

                Log.d(TAG, msg)
//                UtilsFunctions.showToastSuccess(msg)
            })
        return token
    }
}