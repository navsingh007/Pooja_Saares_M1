package com.seasia.poojasarees.common

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.text.InputFilter
import android.text.Spanned
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.reflect.TypeToken
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.constants.AppConstants
import com.seasia.poojasarees.databinding.CustomToastBinding
import com.seasia.poojasarees.model.AddressIn
import com.seasia.poojasarees.model.Addresses
import com.seasia.poojasarees.model.response.AllTownsOut
import com.seasia.poojasarees.utils.DialogClass
import com.seasia.poojasarees.utils.LocaleManager
import com.seasia.poojasarees.utils.PreferenceKeys
import com.seasia.poojasarees.views.auth.LoginActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private val mPhoneList = ArrayList<String>()

object UtilsFunctions {
    @JvmStatic
    fun showToastError(message: String) {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(MyApplication.instance),
                R.layout.custom_toast,
                null,
                false
            ) as CustomToastBinding
        val toast = Toast(MyApplication.instance)
        binding.tvText.text = message
        binding.image.setImageResource(R.drawable.ic_cross)
        binding.toastLayoutRoot.setBackgroundColor(MyApplication.instance.resources.getColor(R.color.colorRed))
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.view = binding.root
        toast.show()
    }

    @JvmStatic
    fun showToastSuccess(message: String) {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(MyApplication.instance),
                R.layout.custom_toast,
                null,
                false
            ) as CustomToastBinding
        val toast = Toast(MyApplication.instance)
        binding.tvText.text = message
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.view = binding.root
        // val view = toast.view
        // val group = toast.view as ViewGroup
        // val messageTextView = group.getChildAt(0) as TextView
        // messageTextView.textSize = 15.0f
        //messageTextView.gravity = Gravity.CENTER
        // view.setBackgroundColor(ContextCompat.getColor(MyApplication.instance, R.color.colorSuccess))
        toast.show()

    }

    @SuppressLint("SimpleDateFormat")
    fun getParticularDay(amount: Int): String {
        val dateFormat = SimpleDateFormat("MMM dd")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, amount)
        val newDate = calendar.time
        return dateFormat.format(newDate)
    }

    @JvmStatic
    fun checkObjectNull(obj: Any?): Boolean {
        return obj != null
    }

    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getAndroidID(): String {
        return Settings.Secure.getString(
            MyApplication.instance.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun isNetworkConnected(): Boolean {
        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork: NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo

        return if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } else {
            showToastWarning(MyApplication.instance.getString(R.string.internet_connection))
            // Toast.makeText(MyApplication.getInstance().getApplicationContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            false
        }
    }


    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun isNetworkConnectedWithoutToast(): Boolean {
        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork: NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo

        return if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } else {
            // showToastWarning(MyApplication.instance.getString(R.string.internet_connection))
            // Toast.makeText(MyApplication.getInstance().getApplicationContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            false
        }
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun isNetworkConnectedReturn(): Boolean {
        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork: NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo

        return if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            activeNetwork.isConnectedOrConnecting
        } else {
            false
        }
    }

    @JvmStatic
    fun showToastWarning(message: String?) {
        if (message == null)
            return
        val inflater =
            MyApplication.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.layout_toast, null)
        val image = layout.findViewById<ImageView>(R.id.image)
        image.setImageResource(R.drawable.ic_warning)
        val text = layout.findViewById<TextView>(R.id.text)
        text.text = message
        val toast = Toast(MyApplication.instance)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = LENGTH_SHORT
        layout.setBackgroundColor(
            ContextCompat.getColor(
                MyApplication.instance,
                R.color.colorOrange
            )
        )
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.view = layout
        toast.show()

    }

    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun addDatetoCurrentDate(add: Int): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, add)
        return dateFormat.format(c.time)

    }

    @SuppressLint("SimpleDateFormat")
    fun getTodayDayName(): String {
        val sdf = SimpleDateFormat("EEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        return dayOfTheWeek.toLowerCase()
    }

    fun getRandomColor(): String {
        val colors = ArrayList<String>()
        colors.add("#F366E0")
        colors.add("#F98D38")
        colors.add("#3A91E2")
        colors.add("#6FBA68")
        colors.add("#9FA8DA")
        colors.add("#DC4378")
        colors.add("#AED581")
        colors.add("#C155C8")
        colors.add("#ECC94A")
        colors.add("#4DD0E1")
        colors.add("#F3735A")
        colors.add("#31BFF0")
        val r = Random()
        val i = r.nextInt(11 - 0) + 0
        return colors[i]
    }

    @JvmStatic
    fun hideKeyBoard(view: View) {
        val imm = MyApplication.instance
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @JvmStatic
    fun hideKeyboardActivity(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboardFromFragment(
        context: Context,
        editText: View
    ) {
        val imm =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    @JvmStatic
    fun setLocalImage(context: Context, path: String, imageView: ImageView) {
        Glide.with(context)
            .load(File(path))
            .placeholder(R.drawable.user)
            .into(imageView)
    }

    @JvmStatic
    fun loadImage(
        context: Context,
        url: String,
        reqOptions: RequestOptions,
        resourceId: Int,
        imageView: ImageView
    ) {
        Glide.with(context)
            .load(url)
            .apply(reqOptions)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(resourceId)
            .into(imageView)
    }

/*    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradiant(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = activity.window
            val background: Drawable =
                activity.resources.getDrawable(R.drawable.gradient_theme)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(activity.resources.getColor(android.R.color.transparent))
            window.setNavigationBarColor(
                activity.resources.getColor(android.R.color.transparent)
            )
            window.setBackgroundDrawable(background)
        }
    }*/

    /**
     * App specific methods
     */
/*    @JvmStatic
    fun getMaritalStatusCode(maritalStatus: String): String {
        return when (maritalStatus) {
            AppConstants.STATUS_MARRIED -> AppConstants.CODE_MARRIED
            AppConstants.STATUS_UNMARRIED -> AppConstants.CODE_UNMARRIED
            AppConstants.STATUS_DIVORCED -> AppConstants.CODE_DIVORCED
            else -> ""
        }
    }

    @JvmStatic
    fun getMaritalStatusValue(maritalStatus: String): String {
        return when (maritalStatus) {
            AppConstants.CODE_MARRIED -> AppConstants.STATUS_MARRIED
            AppConstants.CODE_UNMARRIED -> AppConstants.STATUS_UNMARRIED
            AppConstants.CODE_DIVORCED -> AppConstants.STATUS_DIVORCED
            else -> ""
        }
    }

    @JvmStatic
    fun getGenderCode(gender: String): String {
        return when (gender) {
            AppConstants.GENDER_MALE -> AppConstants.CODE_MALE
            AppConstants.GENDER_FEMALE -> AppConstants.CODE_FEMALE
            AppConstants.GENDER_OTHER -> AppConstants.CODE_OTHER
            else -> ""
        }
    }

    @JvmStatic
    fun getGenderValue(gender: String): String {
        return when (gender) {
            AppConstants.CODE_MALE -> AppConstants.GENDER_MALE
            AppConstants.CODE_FEMALE -> AppConstants.GENDER_FEMALE
            AppConstants.CODE_OTHER -> AppConstants.GENDER_OTHER
            else -> ""
        }
    }*/

    fun priceStringToInt(price: String): Int {
        return price.toDouble().toInt()
    }

    fun setPriceAndSplPrice(
        formattedPrice: String,
        formattedSpecialPrice: String,
        tvPrice: TextView,
        tvSplPrice: TextView
    ) {
        if (!formattedPrice.isEmpty()) {
            val price = "${AppConstants.CURRENCY}${formattedPrice}/-"
            tvPrice.text = price
        }

        // Strike through off price
        if (!formattedPrice.equals(formattedSpecialPrice) && !formattedSpecialPrice.isEmpty() && !formattedPrice.isEmpty()) {
            tvPrice.setPaintFlags(tvPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

            val strikedPrice = "${AppConstants.CURRENCY}${formattedPrice}/-"
            tvPrice.text = strikedPrice

            val specialPrice = "${AppConstants.CURRENCY}${formattedSpecialPrice}/-"
            tvSplPrice.text = specialPrice

            tvPrice.visibility = View.VISIBLE
        } else {
            if (!formattedPrice.isEmpty()) {
                val specialPrice = "${AppConstants.CURRENCY}${formattedPrice}/-"
                tvSplPrice.text = specialPrice
            }
            tvPrice.visibility = View.INVISIBLE
        }
    }

    fun sessionExpired(context: Context) {
        // Empty local strings on session close
        MyApplication.sharedPref.saveString(PreferenceKeys.CUSTOMER_ID, "")
        MyApplication.sharedPref.saveString(PreferenceKeys.CUSTOMER_TOKEN, "")
        MyApplication.sharedPref.saveBoolean(PreferenceKeys.IS_LOGIN, false)

        context.startActivity(
            Intent(context, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    .putExtra("message", it.message)
        )
//        (context as Activity).finish()
    }

    fun serverErrorDialog(context: Context) {
        val dialog = DialogClass().commonConfirmationDialog(
            context,
            context.getString(R.string.serverErrorTitle),
            context.getString(R.string.serverErrorTitle),
            context.getString(R.string.serverErrorDesciption)
        )
        val btnOk = dialog.findViewById<Button>(R.id.btnOK)
        btnOk.setOnClickListener {
            // Finish activity
            (context as Activity).finish()
            // Finish process
//            android.os.Process.killProcess(android.os.Process.myPid())
        }
        dialog.show()
    }

    fun finishAppProcess() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    fun comingSoonDialog(context: Context) {
        val dialog = DialogClass().commonConfirmationDialog(
            context,
            context.getString(R.string.dialog_coming_soon),
            context.getString(R.string.dialog_coming_soon),
            context.getString(R.string.dialogWorkInprogress)
        )
        val btnOk = dialog.findViewById<Button>(R.id.btnOK)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun isValidPassword(password: String?): Boolean {
        return true
//        val pattern: Pattern
//        val matcher: Matcher
//        val PASSWORD_PATTERN =
//            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
//        pattern = Pattern.compile(PASSWORD_PATTERN)
//        matcher = pattern.matcher(password)
//        return matcher.matches()
    }

    fun isValidEmail(email: String): Boolean {
        val pattern =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

//        val pattern =
//            "\\A[A-Za-z0-9!#$%&'+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'+/=?^_`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z"
        return email.matches(pattern.toRegex())
    }


    fun filterCharactersForName(): InputFilter {
        val hindiString =
            " ँ, ं, ः, अ, आ, इ, ई, उ, ऊ, ऋ, ए, ऐ, ऑ, ओ, औ, क, ख, ग, घ, च, छ, ज, झ, ञ, ट, ठ, ड, ढ, ण, त, थ, द, ध, न, प, फ, ब, भ, म, य, र, ल, व, श, ष, स, ह, ़, ा, ि, ी, ु, ू, ृ, ॅ, े, ै, ॉ, ो, ौ, ्"
        val hindiString2 =
            "ँ ं ःअआइईउऊऋएऐऑओऔकखगघचछजझञटठडढणतथदधनपफबभमयरलवशषसह ़ ा ि ी ु ू ृ ॅ े ै ॉ ो ौ ्"


        val filter: InputFilter = object : InputFilter {
            override fun filter(
                source: CharSequence, start: Int,
                end: Int, dest: Spanned?, dstart: Int, dend: Int
            ): CharSequence? {
                for (i in start until end) {
                    if (!Character.isLetter(source[i]) &&
                        Character.toString(source[i]) != "_" &&
                        Character.toString(source[i]) != "-"
                    ) {
                        return ""
                    }
                }
                return null
            }
        }
        return filter
    }

    fun getUserAddress(userAddressJson: String): ArrayList<Addresses> {
        val myType = object : TypeToken<ArrayList<Addresses>>() {}.type
        return MyApplication.gson.fromJson<ArrayList<Addresses>>(userAddressJson, myType)
    }

    fun getTowns(allTownsJson: String): ArrayList<AllTownsOut> {
        val myType = object : TypeToken<ArrayList<AllTownsOut>>() {}.type
        return MyApplication.gson.fromJson<ArrayList<AllTownsOut>>(allTownsJson, myType)
    }

    // User GPS location
    @JvmStatic
    fun checkGpsEnabled(context: Context): Boolean {
        var isEnabled = false
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(context)
        } else {
            isEnabled = true
        }
        return isEnabled
    }

    @JvmStatic
    fun buildAlertMessageNoGps(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setMessage(context.resources.getString(R.string.register_enable_gps))
            .setCancelable(false)
            .setPositiveButton("Yes",
                { dialog, id -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("No",
                { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.loginColor))
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.loginColor))
            alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.resources.getColor(R.color.loginColor))
        }
        alert.show()
    }

    fun getCityFromGeocode(context: Context, latitude: Double, longitude: Double): String {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses.get(0).locality ?: ""
    }

    fun getLanguageStoreId(context: Context): String {
        return when (LocaleManager.getLanguagePref(context)) {
            LocaleManager.HINDI -> "2"
            LocaleManager.ENGLISH -> "1"
            else -> ""
        }
    }
}
