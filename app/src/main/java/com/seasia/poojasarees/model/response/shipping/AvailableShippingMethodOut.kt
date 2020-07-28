package com.seasia.poojasarees.model.response.shipping

data class AvailableShippingMethodOut(
    val amount: Int = 0,
    val available: Boolean = false,
    val base_amount: Int = 0,
    val carrier_code: String = "",
    val carrier_title: String = "",
    val error_message: String = "",
    val method_code: String = "",
    val method_title: String = "",
    val price_excl_tax: Int = 0,
    val price_incl_tax: Int = 0
)