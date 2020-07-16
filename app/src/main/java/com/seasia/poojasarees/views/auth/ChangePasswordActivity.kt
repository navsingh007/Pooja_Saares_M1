package com.seasia.poojasarees.views.auth

import android.app.Dialog
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seasia.poojasarees.R
import com.seasia.poojasarees.common.UtilsFunctions
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.databinding.ActivityChangePasswordBinding
import com.seasia.poojasarees.utils.DialogClass
import com.seasia.poojasarees.utils.DialogssInterface
import com.seasia.poojasarees.viewmodel.auth.ChangePasswordVM
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class ChangePasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var mobileNo: String
    private lateinit var changePasswordVM: ChangePasswordVM
    private var confirmationDialog: Dialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityChangePasswordBinding
        changePasswordVM = ViewModelProvider(this).get(ChangePasswordVM::class.java)
//        changePasswordVM = ChangePasswordVM(this)
        binding.changePassVM = changePasswordVM

        getExtras()
        changePasswordVM.setPhone(mobileNo)

        changePassResponseObserver()
        setToolbar()
        loadingObserver()
        showUserWarningObserver()
        moveViewToCenterOnKeyboardOpen()
    }

    private fun setToolbar() {
        binding.rlToolbar.tvCommonHeading.setText(resources.getString(R.string.change_password))
    }

    private fun getExtras() {
        mobileNo = intent.getStringExtra("mobile")
    }

    private fun changePassResponseObserver() {
        changePasswordVM.changePassResponse().observe(this, Observer {
            stopProgressDialog()

            if (it != null) {
//                passChangedDialog()

                UtilsFunctions.showToastSuccess(getString(R.string.change_password_confirm_msg))
                finish()
            }
        })
    }

    private fun passChangedDialog() {
        // Change password Alert
        val dialog = DialogClass().commonConfirmationDialog(
            this,
            getString(R.string.change_password_confirm_title),
            getString(R.string.change_password_confirm_title),
            getString(R.string.change_password_confirm_msg)
        )

        val btnOk = dialog.findViewById<Button>(R.id.btnOK)
        btnOk.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    .putExtra("message", it.message)
            )
            finish()
        }
        dialog.show()
    }

    private fun loadingObserver() {
        changePasswordVM.isLoading().observe(this, Observer { loading ->
            if (loading) {
                startProgressDialog()
            } else {
                stopProgressDialog()
            }
        })
    }


    private fun showUserWarningObserver() {
        changePasswordVM.showUserWarning().observe(this, Observer { msg ->
            if (msg != null) {
                when (msg) {
                    ChangePasswordVM.MIN_PASS -> UtilsFunctions.showToastWarning(
                        resources.getString(
                            R.string.change_password_hint
                        )
                    )
                    ChangePasswordVM.INVALID_NEW_PASS -> UtilsFunctions.showToastWarning(
                        resources.getString(
                            R.string.register_warn_password_chars
                        )
                    )
                    ChangePasswordVM.INVALID_CONFIRM_PASS -> UtilsFunctions.showToastWarning(
                        resources.getString(R.string.register_warn_password_chars)
                    )
                    ChangePasswordVM.PASS_NOT_MATCH -> UtilsFunctions.showToastWarning(
                        resources.getString(R.string.change_password_not_match)
                    )
                    ChangePasswordVM.ENTER_NEW_PASS -> UtilsFunctions.showToastWarning(
                        resources.getString(
                            R.string.change_password_enter_new_pass
                        )
                    )
                    ChangePasswordVM.ENTER_CONFIRM_PASS -> UtilsFunctions.showToastWarning(
                        resources.getString(
                            R.string.change_password_enter_confirm_pass
                        )
                    )
                }
            }
        })
    }

    private fun moveViewToCenterOnKeyboardOpen() {
        KeyboardVisibilityEvent.setEventListener(
            this,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    // write your code
                    if (isOpen) {
                        binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_DOWN) })
                    } else {
                        binding.svRoot.post(Runnable { binding.svRoot.fullScroll(View.FOCUS_UP) })
                    }
                }
            })
    }
}