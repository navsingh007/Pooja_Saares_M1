package com.seasia.poojasarees.utils

/*
 * Created by saira on 22-12-2017.
 */


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import com.seasia.poojasarees.R
import com.seasia.poojasarees.callbacks.ChoiceCallBack

class DialogClass {
    private var checkClick = 0

    fun setDefaultDialog(
        mContext: Context,
        mInterface: DialogssInterface,
        mKey: String,
        mTitle: String
    ): Dialog {
        val dialogView = Dialog(mContext)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(mContext),
                R.layout.custom_dialog,
                null,
                false
            )

        dialogView.setContentView(binding.root)
        dialogView.setCancelable(false)
        dialogView.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val yes = dialogView.findViewById<Button>(R.id.yes)
        val no = dialogView.findViewById<Button>(R.id.no)

        // title
        (dialogView.findViewById<View>(R.id.txt_dia) as TextView).text = mTitle

        yes.setOnClickListener {
            mInterface.onDialogConfirmAction(null, mKey)
        }
        no.setOnClickListener {
            mInterface.onDialogCancelAction(null, mKey)
        }
        // Create the AlertDialog object and return it
        return dialogView
    }

    fun commonConfirmationDialog(
        mContext: Context,
        mKey: String,
        title: String,
        description: String
//            mInterface: DialogssInterface
    ): Dialog {
        val dialogView = Dialog(mContext, R.style.transparent_dialog_borderless)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(mContext),
                R.layout.dialog_ok_common,
                null,
                false
            )


        dialogView.setContentView(binding.root)
        dialogView.setCancelable(false)

        dialogView.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        (dialogView.findViewById<View>(R.id.tvTitle) as TextView).text = title
        (dialogView.findViewById<View>(R.id.tvDescription) as TextView).text = description

//        val submit = dialogView.findViewById<Button>(R.id.btn_continue)
//
//
//        submit.setOnClickListener {
//            mInterface.onDialogConfirmAction(null, mKey)
//        }

        return dialogView
    }

    // Thank you dialog
    fun setConfirmationDialog(
        mContext: Context,
        mKey: String,
        layoutID: Int
//            mInterface: DialogssInterface
    ): Dialog {
        val dialogView = Dialog(mContext, R.style.transparent_dialog_borderless)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(mContext),
                layoutID,
                null,
                false
            )


        dialogView.setContentView(binding.root)
        dialogView.setCancelable(true)
        dialogView.setCanceledOnTouchOutside(true)

        dialogView.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
//        val submit = dialogView.findViewById<Button>(R.id.btn_continue)
//
//
//        submit.setOnClickListener {
//            mInterface.onDialogConfirmAction(null, mKey)
//        }

        return dialogView
    }

/*    fun setCancelDialog(
            mContext: Context,
            mInterface: DialogssInterface,
            mKey: String
    ): Dialog {
        val dialogView = Dialog(mContext, R.style.transparent_dialog_borderless)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(
                        LayoutInflater.from(mContext),
                        R.layout.layout_cancel_popup,
                        null,
                        false
                )


        dialogView.setContentView(binding.root)
        dialogView.setCancelable(false)

        dialogView.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val submit = dialogView.findViewById<Button>(R.id.btn_continue)
        val cancel = dialogView.findViewById<Button>(R.id.btn_back)

        submit.setOnClickListener {
            mInterface.onDialogConfirmAction(null, mKey)
        }
        cancel.setOnClickListener {
            mInterface.onDialogCancelAction(null, mKey)
        }
        return dialogView
    }*/


    fun setConfirmationDialog(
        mContext: Context,
        mInterface: ChoiceCallBack,
        mKey: String
    ): Dialog {
        val dialogView = Dialog(mContext)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(mContext),
                R.layout.dialog_image_choice,
                null,
                false
            )

        dialogView.setContentView(binding.root)
        dialogView.setCancelable(true)
        dialogView.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val camera = dialogView.findViewById<LinearLayout>(R.id.ll_camera)
        val gallery = dialogView.findViewById<LinearLayout>(R.id.ll_gallery)
        // Create the AlertDialog object and return it
        camera.setOnClickListener {
            mInterface.photoFromCamera(mKey)
            dialogView.dismiss()
        }
        gallery.setOnClickListener {
            mInterface.photoFromGallery(mKey)
            dialogView.dismiss()
        }
        dialogView.show()
        return dialogView
    }
}
