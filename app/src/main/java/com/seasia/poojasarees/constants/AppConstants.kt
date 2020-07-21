package com.seasia.poojasarees.constants

import com.seasia.poojasarees.R
import com.seasia.poojasarees.api.ApiClient
import com.seasia.poojasarees.application.MyApplication

object AppConstants {
    val IMG_BASE_URL = "${ApiClient.BASE_URL_IMAGE}pub/media/catalog/product"
    val IMG_CATEGORY_BASE_URL = "${ApiClient.BASE_URL_IMAGE}pub/media/catalog/category"
    val GROUP_ID = "3"
    val WEBSITE_ID = "1"
    val DISABLE_AUTO_GROUP_CHANGE = "0"
    val CURRENCY = "Rs "

    // Profile
//    val GENDER_MALE = MyApplication.instance.resources.getString(R.string.profile_gender_male)   //"Male"
//    val GENDER_FEMALE = MyApplication.instance.resources.getString(R.string.profile_gender_female)  //"Female"
//    val GENDER_OTHER = MyApplication.instance.resources.getString(R.string.profile_gender_not_specified)  //"Not Specified"
//    val STATUS_UNMARRIED = MyApplication.instance.resources.getString(R.string.profile_status_unmarried)   //"Unmarried"
//    val STATUS_MARRIED = MyApplication.instance.resources.getString(R.string.profile_status_married)   //"Married"
//    val STATUS_DIVORCED = MyApplication.instance.resources.getString(R.string.profile_status_divorced)  //"Divorced"

    val CODE_MALE = "1"
    val CODE_FEMALE = "2"
    val CODE_OTHER = "3"
    val CODE_MARRIED = "5437"
    val CODE_UNMARRIED = "5438"
    val CODE_DIVORCED = "5439"
}