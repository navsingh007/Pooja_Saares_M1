package com.seasia.poojasarees.model.helper

data class Address(
    var shopName: String = "",
    var street: String = "",
    var town: String = "",
    var townId: String = "",
    var district: String = "",

    // Region
    var region: String = "",
    var regionCode: String = "",
    var stateId: String = "",

    var pincode: String = "",

    var isEditable: Boolean = false,
    var addressIdToEdit: String = ""
)