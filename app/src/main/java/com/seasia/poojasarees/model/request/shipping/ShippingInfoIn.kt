package com.seasia.poojasarees.model.request.shipping

data class ShippingInfoIn(
    val addressInformation: AddressInformation = AddressInformation()
)

data class AddressInformation(
    val billing_address: BillingAddress = BillingAddress(),
    val shipping_address: ShippingAddress = ShippingAddress(),
    val shipping_carrier_code: String = "",
    val shipping_method_code: String = ""
)

data class BillingAddress(
    val city: String = "",
    val country_id: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val postcode: String = "",
    val region: String = "",
    val region_code: String = "",
    val region_id: Int = 0,
    val street: List<String> = listOf(),
    val telephone: String = ""
)

data class ShippingAddress(
    val city: String = "",
    val country_id: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val postcode: String = "",
    val region: String = "",
    val region_code: String = "",
    val region_id: Int = 0,
    val street: List<String> = listOf(),
    val telephone: String = ""
)