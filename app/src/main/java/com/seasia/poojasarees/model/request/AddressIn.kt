package com.seasia.poojasarees.model.request

data class AddressIn(
    var customer: Customer = Customer()
) {

    data class Customer(
        var addresses: ArrayList<Addresse> = arrayListOf(),
        var email: String = "",
        var firstname: String = "",
        var group_id: Int = 0,
        var id: Int = 0,
        var lastname: String = "",
        var store_id: Int = 0,
        var website_id: Int = 0
    )

    data class Addresse(
        var city: String = "",
        var country_id: String = "",
        var customer_id: Int = 0,
        var firstname: String = "",
        var id: Int = 0,
        var lastname: String = "",
        var postcode: String = "",
        var district: String = "",
        var region: Region = Region(),
        var region_id: Int = 0,
        var street: ArrayList<String> = arrayListOf(),
        var telephone: String = ""
    )

    data class Region(
        var region: String = "",
        var region_code: String = "",
        var region_id: Int = 0
    )
}