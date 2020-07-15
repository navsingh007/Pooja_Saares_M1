package com.seasia.poojasarees.core

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.utils.LocaleManager
import com.seasia.poojasarees.utils.PermissionUtil
import com.seasia.poojasarees.utils.PrefStore
import java.util.ArrayList
import java.util.HashMap


abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: Dialog? = null
    protected var viewDataBinding: ViewDataBinding? = null

    var permCallback: PermissionCallback? = null

    //    var sharedPref: PrefStore? = null
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FacebookSdk.sdkInitialize(applicationContext)
        resetTitles()

        setContentView(getLayoutId())

        this@BaseActivity.overridePendingTransition(
            R.anim.slide_in,
            R.anim.slide_out
        )

        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initializeProgressDialog()

//        sharedPref = PrefStore(this)

        initViews()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initViews()


    fun checkSelfPermission(perms: Array<String>, permCallback: PermissionCallback) {
        this.permCallback = permCallback
        ActivityCompat.requestPermissions(this, perms, 99)
    }

/*    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this,
            permCallback
        )
    }*/


    interface PermissionCallback {
        fun permGranted()
        fun permDenied()
    }


    @SuppressLint("MissingPermission")
    fun isNetworkConnectedDefault(): Boolean {
        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork: NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            return activeNetwork.isConnectedOrConnecting
        } else {
//            showSnackBar(getString(R.string.internet_connection))
            //  Toast.makeText(MyApplication.getInstance().getApplicationContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            return false
        }
    }


    private fun initializeProgressDialog() {
        progressDialog = Dialog(this, R.style.transparent_dialog_borderless)
        val view = View.inflate(this, R.layout.layout_progress_dialog, null)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(view)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //    mFragmentManager = supportFragmentManager

        // txtMsgTV = (TextView) view.findViewById(R.id.txtMsgTV);
        progressDialog!!.setCancelable(false)
    }

    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

/*    fun showSnackBar(message: String?) {
        if (message == null)
            return
        SnackbarManager.show(
            Snackbar.with(MyApplication.instance) // context
                .text(message) // text to be displayed
                .type(SnackbarType.MULTI_LINE)
                .swipeToDismiss(true)
                .position(Snackbar.SnackbarPosition.BOTTOM)
                .actionLabel(null)
                .textColor(Color.WHITE) // change the text color
                .textTypeface(Typeface.DEFAULT) // change the text font
                .animation(true)
            //       .color(ContextCompat.getColor(this, R.color.colorOrange)) // change the background color                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
            , this
        ) // activity where it is displayed
    }*/

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     *  Locales
     */
    protected open fun resetTitles() {
        try {
            val info = packageManager.getActivityInfo(
                componentName,
                PackageManager.GET_META_DATA
            )
            if (info.labelRes != 0) {
                setTitle(info.labelRes)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase))
//        super.attachBaseContext(LocaleManager.setLocale(MyApplication.instance))
    }

    public fun checkAndRequestPermissionLocation(): Boolean {
        val permissionLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    public fun checkAndRequestPermissionSms(): Boolean {
        val permissionLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)

        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    public fun checkAndRequestPermissions(): Boolean {
        val camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writepermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionRecordAudio =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)


        val listPermissionsNeeded = ArrayList<String>()

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.RECORD_AUDIO] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    // Check for both permissions
                    if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED
                    ) {
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.RECORD_AUDIO
                            )

                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        ) {
/*                            showDialogOK("Service Permissions are required for this app",
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            // proceed with logic by disabling the related features or quit the app.
                                            finish()
                                    }
                                })*/
                        } else {
//                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")

                            //                            //proceed with logic by disabling the related features or quit the app.
                        }//permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                    }
                }
            }
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }
}
