package com.seasia.poojasarees.model

import com.seasia.poojasarees.model.response.authentication.CustomAttribute
import com.seasia.poojasarees.model.response.authentication.LoginOut
import java.io.Serializable

data class AddressIn(
    var customer: Customer = Customer()
) {
    data class Customer(
//        var loginOut: LoginOut? = null
        var addresses: ArrayList<Addresses> = arrayListOf(),
        var custom_attributes: ArrayList<CustomAttribute> = arrayListOf(),
        var disable_auto_group_change: Int = 0,
        var email: String = "",
        var firstname: String = "",
        var group_id: Int = 0,
        var id: Int = 0,
        var lastname: String = "",
        var store_id: Int = 0,
//        var taxvat: String = "",
        var website_id: Int = 0
    ): Serializable
}

data class Addresses(
    var city: String = "",
    var district: String = "",
    var country_id: String = "",
//    var custom_attributes: CustomAttributes = CustomAttributes(),
    var customer_id: String = "",
    var default_billing: Boolean = false,
    var default_shipping: Boolean = false,
    var firstname: String = "",
    var id: String = "",
    var lastname: String = "",
    var postcode: String = "",
    var prefix: String = "",
    var region: Region = Region(),
    var region_id: String = "",
    var street: ArrayList<String> = arrayListOf(),
    var telephone: String = ""
): Serializable

/*data class CustomAttributes(
    var district: District = District()
): Serializable

data class District(
    var attribute_code: String = "",
    var value: String = ""
): Serializable*/

data class Region(
    var region: String = "",
    var region_code: String = "",
    var region_id: Int = 0
): Serializable

/*data class CustomAttribute(
    var attribute_code: String = "",
    var value: String = ""
): Serializable*/
