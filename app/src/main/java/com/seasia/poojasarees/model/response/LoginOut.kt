package com.seasia.poojasarees.model.response

import com.google.gson.annotations.SerializedName

data class LoginOut(
    val addresses: ArrayList<Addresses>? = arrayListOf(),
    val cmsLinks: CmsLinks? = CmsLinks(),
    val created_at: String? = "",
    val created_in: String? = "",
    val custom_attributes: ArrayList<CustomAttribute>? = arrayListOf(),
    val customerToken: String? = "",
    val disable_auto_group_change: String? = "",
    val email: String? = "",
    val extension_attributes: ExtensionAttributes? = ExtensionAttributes(),
    val firstname: String? = "",
    val group_id: Int? = 0,
    val gender: Int? = 0,
    val id: Int? = 0,
    val lastname: String? = "",
    val message: String? = "",
    val store_id: Int? = 0,
    val taxvat: String? = "",
    val updated_at: String? = "",
    val website_id: Int? = 0
) {

    data class Addresses(
        val city: String? = "",
        val country_id: String? = "",
        val customer_id: String? = "",
        val default_billing: Boolean? = false,
        val default_shipping: Boolean? = false,
        val firstname: String? = "",
        val id: String? = "",
        val lastname: String? = "",
        val postcode: String? = "",
        val prefix: String? = "",
        val region: Region? = Region(),
        val region_id: String? = "",
        val street: ArrayList<String>? = arrayListOf(),
        val telephone: String? = ""
    )

    data class CmsLinks(
        val about_us: String? = "",
        val privacy_policy: String? = "",
        val terms_conditions: String? = ""
    )

    data class CustomAttribute(
        val attribute_code: String? = "",
        val value: String? = ""
    )

    class ExtensionAttributes(
    )

    data class Region(
        val region: String? = "",
        val region_code: String? = "",
        val region_id: Int? = 0
    )
}