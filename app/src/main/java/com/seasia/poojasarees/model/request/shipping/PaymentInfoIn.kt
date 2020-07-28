package com.seasia.poojasarees.model.request.shipping

data class PaymentInfoIn(
    val billing_address: BillingAddress = BillingAddress(),
    val paymentMethod: PaymentMethod = PaymentMethod()
)

data class PaymentMethod(
    val method: String = ""
)