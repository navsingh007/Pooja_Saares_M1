package com.seasia.poojasarees.model.response

data class LoginOutOld(
    val addresses: ArrayList<Any>? = arrayListOf(),
    val created_at: String? = "",
    val created_in: String? = "",
    val custom_attributes: ArrayList<CustomAttribute>? = arrayListOf(),
    val disable_auto_group_change: String? = "",
    val email: String? = "",
    val message: String? = "",
    val extension_attributes: ExtensionAttributes? = ExtensionAttributes(),
    val firstname: String? = "",
    val group_id: String? = "",
    val id: String? = "",
    val lastname: String? = "",
    val store_id: String? = "",
    val updated_at: String? = "",
    val website_id: String? = ""
) {

    data class CustomAttribute(
        val attribute_code: String? = "",
        val value: String? = ""
    )

    class ExtensionAttributes(
    )
}