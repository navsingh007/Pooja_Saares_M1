package com.seasia.poojasarees.core


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.utils.PrefStore


/**
 * A simple [Fragment] subclass.
 *
 */
abstract class BaseFragment : Fragment() {
    var viewDataBinding: ViewDataBinding? = null
    private var progressDialog: Dialog? = null
    var permCallback: PermissionCallback? = null
    //    var sharedPref: PrefStore? = null
    var mContext: Context? = null

    lateinit var baseActivity : BaseActivity

    override fun onAttach(context : Context) {
        super.onAttach(context)
        if (context is BaseActivity)
            baseActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        val viewRootBinding = viewDataBinding!!.root
        initializeProgressDialog()
        mContext = context
//        sharedPref = PrefStore(mContext as Activity)

        baseActivity.overridePendingTransition(
            R.anim.slide_in,
            R.anim.slide_out
        )

        return viewRootBinding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    protected abstract fun initView()
    protected abstract fun getLayoutResId(): Int


    fun checkSelfPermission(perms: Array<String>, permCallback: PermissionCallback) {
        this.permCallback = permCallback
        ActivityCompat.requestPermissions(requireActivity(), perms, 99)
    }


    interface PermissionCallback {
        fun permGranted()

        fun permDenied()
    }

    @SuppressLint("MissingPermission")
    fun isNetworkConnectedDefault(): Boolean {

        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork: NetworkInfo? = null
        if (cm != null)
            activeNetwork = cm.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } else {
//            showSnackBar(getString(R.string.internet_connection))
            //  Toast.makeText(MyApplication.getInstance().getApplicationContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            return false
        }
    }


    private fun initializeProgressDialog() {
        progressDialog = Dialog(requireContext(), R.style.transparent_dialog_borderless)
        val view = View.inflate(activity, R.layout.layout_progress_dialog, null)
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
            Snackbar.with(activity!!.applicationContext) // context
                .text(message) // text to be displayed
                .type(SnackbarType.MULTI_LINE)
                .swipeToDismiss(true)
                .position(Snackbar.SnackbarPosition.BOTTOM)
                .actionLabel(null)
                .textColor(Color.WHITE) // change the text color
                .textTypeface(Typeface.DEFAULT) // change the text font
                .animation(true)
                //                        .color(Color.BLUE) // change the background color
                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT), activity!!
        ) // activity where it is displayed
    }*/
}