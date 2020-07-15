package com.seasia.poojasarees.constants

import android.content.Context
import com.seasia.poojasarees.R

class ProfileConstants(context: Context) {
    val GENDER_MALE = context.resources.getString(R.string.profile_gender_male)   //"Male"
    val GENDER_FEMALE = context.resources.getString(R.string.profile_gender_female)  //"Female"
    val GENDER_OTHER =
        context.resources.getString(R.string.profile_gender_not_specified)  //"Not Specified"
    val STATUS_UNMARRIED =
        context.resources.getString(R.string.profile_status_unmarried)   //"Unmarried"
    val STATUS_MARRIED = context.resources.getString(R.string.profile_status_married)   //"Married"
    val STATUS_DIVORCED =
        context.resources.getString(R.string.profile_status_divorced)  //"Divorced"
}