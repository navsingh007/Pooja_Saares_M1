package com.seasia.poojasarees.views.payment

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.payumoney.core.PayUmoneyConfig
import com.payumoney.core.PayUmoneyConstants
import com.payumoney.core.PayUmoneySdkInitializer
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import com.payumoney.sdkui.ui.utils.ResultModel
import com.seasia.poojasarees.R
import com.seasia.poojasarees.application.MyApplication
import com.seasia.poojasarees.constants.AppConstants
import com.seasia.poojasarees.constants.PaymentEnvironment
import com.seasia.poojasarees.core.BaseActivity
import com.seasia.poojasarees.utils.PreferenceKeys
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class PaymentActivity : BaseActivity() {
    private lateinit var paymentEnvironment: PaymentEnvironment
    private val isDisableExitConfirmation = false
    private val amount = "1"

    override fun getLayoutId(): Int {
        return R.layout.activity_payment
    }

    override fun initViews() {
        paymentEnvironment = PaymentEnvironment.SANDBOX

        // HARDcoded amount
        btnCheckout.setOnClickListener {
            launchPaymentFlow(amount, AppConstants.CURRENCY)
            btnCheckout.isEnabled = false
        }
    }

    private fun launchPaymentFlow(
        amount: String,
        currency: String
    ) {
        val payUmoneyConfig: PayUmoneyConfig = PayUmoneyConfig.getInstance()
        payUmoneyConfig.setPayUmoneyActivityTitle("Pooja Sarees")
        payUmoneyConfig.setDoneButtonText("Pay $currency $amount")
        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation)

        val builder: PayUmoneySdkInitializer.PaymentParam.Builder =
            PayUmoneySdkInitializer.PaymentParam.Builder()
        builder.setAmount(amount)
            .setTxnId(System.currentTimeMillis().toString() + "")
            .setPhone(MyApplication.sharedPref.getString(PreferenceKeys.PHONE_NO, ""))
            .setProductName("Order Information")
            .setFirstName(MyApplication.sharedPref.getString(PreferenceKeys.FIRST_NAME, ""))
            .setEmail(MyApplication.sharedPref.getString(PreferenceKeys.EMAIL, ""))
            .setsUrl(paymentEnvironment.surl())
            .setfUrl(paymentEnvironment.furl())
            .setUdf1("")
            .setUdf2("")
            .setUdf3("")
            .setUdf4("")
            .setUdf5("")
            .setUdf6("")
            .setUdf7("")
            .setUdf8("")
            .setUdf9("")
            .setUdf10("")
            .setIsDebug(paymentEnvironment.debug())
            .setKey(paymentEnvironment.merchant_Key())
            .setMerchantId(paymentEnvironment.merchant_ID())
        try {
            val mPaymentParams: PayUmoneySdkInitializer.PaymentParam = builder.build()
            val stringBuilder = StringBuilder()
            val params: HashMap<String, String> = mPaymentParams.getParams()
            stringBuilder.append(params[PayUmoneyConstants.KEY].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.TXNID].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.AMOUNT].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.PRODUCT_INFO].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.FIRSTNAME].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.EMAIL].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.UDF1].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.UDF2].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.UDF3].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.UDF4].toString() + "|")
            stringBuilder.append(params[PayUmoneyConstants.UDF5].toString() + "||||||")
            //calculateHashInServer(mPaymentParams);
            stringBuilder.append(paymentEnvironment.salt())
            val hash = hashCal(stringBuilder.toString())
            mPaymentParams.setMerchantHash(hash)
            PayUmoneyFlowManager.startPayUMoneyFlow(
                mPaymentParams,
                this@PaymentActivity,
                R.style.AppTheme_default,
                true
            )
            // calculateHashInServer(mPaymentParams);
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            btnCheckout.isEnabled = true
        }
    }

    fun hashCal(str: String): String {
        val hashseq = str.toByteArray()
        val hexString = StringBuilder()
        try {
            val algorithm =
                MessageDigest.getInstance("SHA-512")
            algorithm.reset()
            algorithm.update(hashseq)
            val messageDigest = algorithm.digest()
            for (aMessageDigest in messageDigest) {
                val hex = Integer.toHexString(0xFF and aMessageDigest.toInt())
                if (hex.length == 1) {
                    hexString.append("0")
                }
                hexString.append(hex)
            }
        } catch (ignored: NoSuchAlgorithmException) {
        }
        return hexString.toString()
    }

    /**
     *  Currently finishing this Activity and will show result in previous Activity
     */
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        btnCheckout.isEnabled = true
        val intent = Intent()
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            val transactionResponse: TransactionResponse =
                data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)
            val resultModel: ResultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT)
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
                    // showAlert("Payment Successful");
                    val res = transactionResponse.getPayuResponse()
                    try {
                        var jsonObject = JSONObject(res)
                        val result = jsonObject.getString("result")
                        Log.d("id------", result)
                        jsonObject = JSONObject(result)
                        val id = jsonObject.getString("paymentId")
                        Log.d("id------", id)
                        intent.putExtra("status", "success")
                        intent.putExtra("id", id)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    // merchantHash = jsonObject.getString("result");
                } else if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.CANCELLED) {
                    //  showAlert("Payment Cancelled");
                    intent.putExtra("status", "Payment Cancelled")
                } else if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.FAILED) {
                    //showAlert("Payment Failed");
                    intent.putExtra("status", "Payment Failed")
                }
            } else if (resultModel != null && resultModel.error != null) {
                Toast.makeText(this, "Error check log", Toast.LENGTH_SHORT).show()
                intent.putExtra("status", "SomethingWent wrong")
            } else {
                Toast.makeText(this, "Both objects are null", Toast.LENGTH_SHORT).show()
                intent.putExtra("status", "SomethingWent wrong")
            }
            setResult(RESULT_OK, intent)
            finish()
        } else if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_CANCELED) {
            //   showAlert("Payment Cancelled");
            intent.putExtra("status", "Payment Cancelled")
            setResult(RESULT_OK, intent)
            finish()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        btnCheckout.isEnabled = true
//    }
}