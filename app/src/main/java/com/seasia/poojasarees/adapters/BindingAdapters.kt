package com.seasia.poojasarees.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContextWrapper
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.utils.FontStyle
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {
    @BindingAdapter("android:src")
    @JvmStatic
    fun setImageViewResource(imageView: ImageView, resource: String) {
        var drawable1 = MyApplication.instance.resources.getDrawable(R.drawable.user)

        if (resource.contains("set_default")) {
            drawable1 = try {
                val name = resource.split("set_default#")[1]
                val id = MyApplication.instance.resources
                    .getIdentifier(name, "drawable", MyApplication.instance.packageName)
                MyApplication.instance.resources.getDrawable(id)

            } catch (e: Exception) {
                MyApplication.instance.resources.getDrawable(R.drawable.user)
            }
        }
        Glide.with(imageView.context)
            .load(resource)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(drawable1)
            .into(imageView)
    }

    @BindingAdapter("backOnClick")
    @JvmStatic
    fun bindOnImageButtonClickListener(imageView: ImageView, text: String?) {
        imageView.setOnClickListener { view ->
            val activity = getActivity(view)
            println(text)
            activity!!.finish()
        }
    }

    @BindingAdapter("hideKeyBoardOnClick")
    @JvmStatic
    fun bindOnLinearLayoutClickListener(view: View, text: String?) {
        view.setOnClickListener { view ->
            UtilsFunctions.hideKeyBoard(view)
        }
    }

    @BindingAdapter("android:visibility")
    @JvmStatic
    fun setVisibility(view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE

    }

    private fun getActivity(v: View): Activity? {
        var context = v.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = (context).baseContext
        }
        return null
    }

    @BindingAdapter("bind:font")
    @JvmStatic
    fun setFont(textView: TextView, fontName: String) {
        textView.typeface = FontStyle.getInstance().getFont(fontName)
    }

    @BindingAdapter("editTextOnClick")
    @JvmStatic
    fun bindOnEditTextClickListener(editText: EditText, text: String?) {
        var strDate = ""
        editText.setOnClickListener { view ->
            val activity = getActivity(view)
            val calendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "yyyy-MM-dd"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    strDate = sdf.format(calendar.time)
                    editText.setText(strDate)
                }
            val dpDialog = DatePickerDialog(activity!!, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Disable future dates
            dpDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            dpDialog.setOnShowListener {
                dpDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(MyApplication.instance.resources.getColor(R.color.loginColor))
                dpDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(MyApplication.instance.resources.getColor(R.color.loginColor))
                dpDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(MyApplication.instance.resources.getColor(R.color.loginColor))
            }
            dpDialog.show()
        }

    }

    @BindingAdapter("disablePastDateOnEdtText")
    @JvmStatic
    fun disablePastDateOnEdtTextClick(editText: EditText, text: String?) {
        var strDate = ""
        editText.setOnClickListener { view ->
            val activity = getActivity(view)
            val calendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "yyyy/MM/dd"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    strDate = sdf.format(calendar.time)
                    editText.setText(strDate)
                }
            val dpDialog = DatePickerDialog(activity!!, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dpDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dpDialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("editTextTimeOnClick")
    @JvmStatic
    fun bindOnTimeEditTextClickListener(editText: EditText, text: String?) {
        editText.setOnClickListener { view ->
            val activity = getActivity(view)
            val c = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR_OF_DAY)
            val mMinute = c.get(Calendar.MINUTE)
            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> editText.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
            timePickerDialog.show()
        }
    }
}

