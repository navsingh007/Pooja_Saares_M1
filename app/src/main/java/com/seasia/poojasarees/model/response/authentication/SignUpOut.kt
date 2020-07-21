package com.seasia.poojasarees.model.response.authentication

data class SignUpOut(
    val addresses: ArrayList<Any>? = arrayListOf(),
    val created_at: String? = "",
    val created_in: String? = "",
    val custom_attributes: ArrayList<CustomAttribute>? = arrayListOf(),
    val disable_auto_group_change: Int? = 0,
    val email: String? = "",
    val extension_attributes: ExtensionAttributes? = ExtensionAttributes(),
    val firstname: String? = "",
    val group_id: Int? = 0,
    val id: Int? = 0,
    val lastname: String? = "",
    val store_id: Int? = 0,
    val updated_at: String? = "",
    val website_id: Int? = 0
) {

    data class CustomAttribute(
        val attribute_code: String? = "",
        val value: String? = ""
    )

    data class ExtensionAttributes(
        val is_subscribed: Boolean? = false
    )
}