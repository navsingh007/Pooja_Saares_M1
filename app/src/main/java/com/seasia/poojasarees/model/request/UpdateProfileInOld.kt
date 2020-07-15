package com.seasia.poojasarees.model.request

data class UpdateProfileInOld(
    val customer: Customer = Customer()
) {
    data class Customer(
        var custom_attributes: ArrayList<CustomAttribute> = arrayListOf(),
        var disable_auto_group_change: String? = "",
        var email: String? = "",
        var firstname: String? = "",
        var group_id: String? = "",
        var id: String? = "",
        var lastname: String? = "",
        var store_id: Int? = 0,
        var taxvat: String? = "",
        var dob: String? = "",
        var website_id: String? = ""
    )

    data class CustomAttribute(
        var attribute_code: String? = "",
        var value: String? = ""
    )
}