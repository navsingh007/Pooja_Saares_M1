package com.seasia.poojasarees.model.request

import java.io.Serializable

data class SignUpIn(
    var customer: Customer = Customer(),
    var password: String = ""
): Serializable {

    data class Customer(
        var custom_attributes: ArrayList<CustomAttribute> = arrayListOf(),
        var email: String = "",
        var firstname: String = "",
        var group_id: String = "",
        var store_id: String = "",
        var lastname: String = "",
        var website_id: String = ""
    ): Serializable

    data class CustomAttribute(
        var attribute_code: String = "",
        var value: String = ""
    ): Serializable
}