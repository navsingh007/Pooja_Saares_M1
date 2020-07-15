package com.seasia.poojasarees.model.helper

import java.io.Serializable

data class Profile(
    var name: String = "",
    var lastName: String = "",
    var shopName: String = "",
    var mobileNo: String = "",
    var emailId: String = "",
    var gstNo: String = "",
    var street: String = "",
    var town: String = "",
//    var city: String = "",
    var state: String = "",
    var pincode: String = "",

    var dob: String = "",
    var gender: String = "",
    var maritalStatus: String = "",
    var education: String = "",
    var languagesKnown: String = "",
    var hobbies: String = ""
): Serializable