package com.seasia.poojasarees.model.response.orderpayment

data class PlaceOrderIn(
    val paymentMethod: PaymentMethod = PaymentMethod()
)

data class PaymentMethod(
    val method: String = ""
)