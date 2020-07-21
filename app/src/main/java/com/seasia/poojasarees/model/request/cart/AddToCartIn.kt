package com.seasia.poojasarees.model.request.cart

data class AddToCartIn(
    val cartItem: CartItem = CartItem()
) {

    data class CartItem(
        var qty: Int = 0,
        var quote_id: String = "",
        var sku: String = ""
    )
}