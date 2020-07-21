package com.seasia.poojasarees.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.seasia.poojasarees.R;
import com.seasia.poojasarees.application.MyApplication;
import com.seasia.poojasarees.core.BaseActivity;


public final class PermissionUtil {

    private static final int REQUEST_CODE = 99;

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults, BaseActivity context, BaseActivity.PermissionCallback permCallback) {
        boolean permGrantedBool = false;
        if (requestCode == REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
//                        context.showSnackBar(context.getString(R.string.not_sufficient_permissions)
//                                + context.getString(R.string.app_name)
//                                + context.getString(R.string.permissions));
                    permGrantedBool = false;
                    break;
                } else {
                    permGrantedBool = true;
                }
            }
            if (permCallback != null) {
                if (permGrantedBool)
                    permCallback.permGranted();
                else {
//                        showPermissionDialog(context);
                    permCallback.permDenied();
                }
            }
        }
    }

    public static boolean isNetworkConnectedDefault() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.instance.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null)
            activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(MyApplication.instance.getApplicationContext(), MyApplication.instance.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}