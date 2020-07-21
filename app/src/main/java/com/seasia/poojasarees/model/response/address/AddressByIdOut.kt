package com.seasia.poojasarees.model.response.address

data class AddressByIdOut(
    val city: String? = "",
    val country_id: String? = "",
    val customer_id: Int? = 0,
    val default_billing: Boolean? = false,
    val default_shipping: Boolean? = false,
    val firstname: String? = "",
    val id: Int? = 0,
    val lastname: String? = "",
    val postcode: String? = "",
    val prefix: String? = "",
    val region: Region? = Region(),
    val region_id: Int? = 0,
    val street: List<String>? = listOf(),
    val telephone: String? = ""
) {

    data class Region(
        val region: String? = "",
        val region_code: String? = "",
        val region_id: Int? = 0
    )
}