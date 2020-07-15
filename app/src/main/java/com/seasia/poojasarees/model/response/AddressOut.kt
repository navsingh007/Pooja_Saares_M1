package com.seasia.poojasarees.model.response

import java.io.Serializable

data class AddressOut(
    val addresses: ArrayList<Addresse>? = arrayListOf(),
    val created_at: String? = "",
    val created_in: String? = "",
    val custom_attributes: ArrayList<CustomAttribute>? = arrayListOf(),
    val disable_auto_group_change: Int? = 0,
    val email: String? = "",
    val extension_attributes: ExtensionAttributes? = ExtensionAttributes(),
    val firstname: String? = "",
    val gender: Int? = 0,
    val group_id: Int? = 0,
    val id: Int? = 0,
    val lastname: String? = "",
    val store_id: Int? = 0,
    val taxvat: String? = "",
    val updated_at: String? = "",
    val website_id: Int? = 0
): Serializable {

    data class Addresse(
        val city: String? = "",
        val country_id: String? = "",
        val customer_id: Int? = 0,
        val firstname: String? = "",
        val id: Int? = 0,
        val lastname: String? = "",
        val postcode: String? = "",
        val region: Region? = Region(),
        val region_id: Int? = 0,
        val street: ArrayList<String>? = arrayListOf(),
        val telephone: String? = ""
    ): Serializable

    data class CustomAttribute(
        val attribute_code: String? = "",
        val value: String? = ""
    ): Serializable

    data class ExtensionAttributes(
        val is_subscribed: Boolean? = false
    ): Serializable

    data class Region(
        val region: String? = "",
        val region_code: String? = "",
        val region_id: Int? = 0
    ): Serializable
}