package com.seasia.poojasarees.model.response.cart

data class AddToCartOut(
    val item_id: Int? = 0,
    val name: String? = "",
    val price: Int? = 0,
    val product_type: String? = "",
    val qty: Int? = 0,
    val quote_id: String? = "",
    val sku: String? = ""
)