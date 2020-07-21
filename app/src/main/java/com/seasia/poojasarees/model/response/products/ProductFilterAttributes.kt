package com.seasia.poojasarees.model.response.products

data class ProductFilterAttributes(
    val availablefilter: Availablefilter? = Availablefilter(),
    val store_id: String? = ""
) {

    data class Availablefilter(
        val Color: ArrayList<Color>? = arrayListOf(),
        val Price: ArrayList<Price>? = arrayListOf(),
        val Size: ArrayList<Size>? = arrayListOf()
    )

    data class Color(
        val count: String? = "",
        val display: String? = "",
        val url: String? = "",
        val value: String? = ""
    )

    data class Price(
        val count: String? = "",
        val display: String? = "",
        val url: String? = "",
        val value: String? = ""
    )

    data class Size(
        val count: String? = "",
        val display: String? = "",
        val url: String? = "",
        val value: String? = ""
    )
}