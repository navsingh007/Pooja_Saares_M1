package com.seasia.poojasarees.model.response.wishlist

data class AllWishlistProductsOut(
    val added_at: String? = "",
    val description: Any? = Any(),
    val product: Product? = Product(),
    val product_id: String? = "",
    val qty: Int? = 0,
    val store_id: String? = "",
    val wishlist_id: String? = "",
    val wishlist_item_id: String? = ""
) {

    data class Product(
        val attribute_set_id: String? = "",
        val brand: String? = "",
        val created_at: String? = "",
        val entity_id: String? = "",
        val final_price: Any? = Any(),
        val has_options: String? = "",
        val image: String? = "",
        val max_price: String? = "",
        val min_price: String? = "",
        val minimal_price: String? = "",
        val msrp_display_actual_price_type: String? = "",
        val name: String? = "",
        val news_from_date: String? = "",
        val price: String? = "",
        val request_path: String? = "",
        val required_options: String? = "",
        val sku: String? = "",
        val small_image: String? = "",
        val special_from_date: String? = "",
        val special_price: String? = "",
        val status: String? = "",
        val store_id: Int? = 0,
        val swatch_image: String? = "",
        val tax_class_id: String? = "",
        val thumbnail: String? = "",
        val tier_price: Any? = Any(),
        val type_id: String? = "",
        val updated_at: String? = "",
        val url_key: String? = "",
        val visibility: String? = ""
    )
}