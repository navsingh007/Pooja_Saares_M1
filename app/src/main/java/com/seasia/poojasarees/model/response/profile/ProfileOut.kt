package com.seasia.poojasarees.model.response.profile

data class ProfileOut(
    val addresses: ArrayList<Addresse>? = arrayListOf(),
    val created_at: String? = "",
    val created_in: String? = "",
    val custom_attributes: ArrayList<CustomAttribute>? = arrayListOf(),
    val default_billing: String? = "",
    val default_shipping: String? = "",
    val disable_auto_group_change: Int? = 0,
    val email: String? = "",
    val extension_attributes: ExtensionAttributes? = ExtensionAttributes(),
    val firstname: String? = "",
    val gender: Int? = 0,
    val group_id: Int? = 0,
    val id: Int? = 0,
    val lastname: String? = "",
    val dob: String? = "",
    val store_id: Int? = 0,
    val taxvat: String? = "",
    val updated_at: String? = "",
    val website_id: Int? = 0
) {

    data class Addresse(
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
        val street: ArrayList<String>? = arrayListOf(),
        val telephone: String? = ""
    )

    data class CustomAttribute(
        val attribute_code: String? = "",
        val value: String? = ""
    )

    data class ExtensionAttributes(
        val is_subscribed: Boolean? = false
    )

    data class Region(
        val region: String? = "",
        val region_code: String? = "",
        val region_id: Int? = 0
    )
}