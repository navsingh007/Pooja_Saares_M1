package com.seasia.poojasarees.model.response

data class AllStatesOut(
    val available_regions: ArrayList<AvailableRegion>? = arrayListOf(),
    val full_name_english: String? = "",
    val full_name_locale: String? = "",
    val id: String? = "",
    val three_letter_abbreviation: String? = "",
    val two_letter_abbreviation: String? = ""
) {

    data class AvailableRegion(
        val code: String? = "",
        val id: String? = "",
        val name: String? = ""
    )
}