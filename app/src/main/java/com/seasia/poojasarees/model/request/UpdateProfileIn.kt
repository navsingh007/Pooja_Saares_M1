package com.seasia.poojasarees.model.request

data class UpdateProfileIn(
    var customer: Customer = Customer()
) {

    data class Customer(
//        var addresses: ArrayList<Addresse> = arrayListOf(),
        var custom_attributes: ArrayList<CustomAttribute> = arrayListOf(),
        var disable_auto_group_change: String = "",
        var email: String = "",
        var firstname: String = "",
        var group_id: String = "",
        var id: String = "",
        var taxvat: String = "",
        var dob: String = "",
        var lastname: String = "",
        var store_id: Int = 0,
        var gender: Int = 0,
        var website_id: String = ""
    )

    data class Addresse(
        var city: String = "",
        var country_id: String = "",
        var customer_id: Int = 0,
        var default_billing: Boolean = false,
        var default_shipping: Boolean = false,
        var firstname: String = "",
        var id: Int = 0,
        var lastname: String = "",
        var postcode: String = "",
        var prefix: String = "",
        var region: Region = Region(),
        var region_id: Int = 0,
        var street: ArrayList<String> = arrayListOf(),
        var telephone: String = ""
    )

    data class CustomAttribute(
        var attribute_code: String = "",
        var value: String = ""
    )

    data class Region(
        var region: String = "",
        var region_code: String = "",
        var region_id: Int = 0
    )
}