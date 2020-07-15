package com.seasia.poojasarees.model.response

data class HomeOut(
    val banners: Banners? = Banners(),
    val brands: ArrayList<Brand>? = arrayListOf(),
    val categories: ArrayList<Category>? = arrayListOf(),
    val newProducts: ArrayList<NewProduct>? = arrayListOf()
) {

    data class Banners(
        val info_banner: ArrayList<InfoBanner>? = arrayListOf(),
        val slider_banner: ArrayList<SliderBanner>? = arrayListOf()
    )

    data class Brand(
        val id: String? = "",
        val image: String? = "",
        val name: String? = ""
    )

    data class Category(
        val image: String? = "",
        val name: String? = ""
    )

    data class NewProduct(
        val attribute_set_id: String? = "",
        val created_at: String? = "",
        val entity_id: String? = "",
        val gift_message_available: String? = "",
        val has_options: String? = "",
        val image: String? = "",
        val meta_description: String? = "",
        val meta_keyword: String? = "",
        val meta_title: String? = "",
        val msrp_display_actual_price_type: String? = "",
        val name: String? = "",
        val news_from_date: String? = "",
        val news_to_date: Any? = Any(),
        val options_container: String? = "",
        val price: String? = "",
        val brand: String? = "",
        val special_price: String? = "",
        val required_options: String? = "",
        val sku: String? = "",
        val small_image: String? = "",
        val status: String? = "",
        val store_id: Int? = 0,
        val swatch_image: String? = "",
        val tax_class_id: String? = "",
        val thumbnail: String? = "",
        val ts_country_of_origin: String? = "",
        val ts_packaging_type: String? = "",
        val type_id: String? = "",
        val updated_at: String? = "",
        val url_key: String? = "",
        val visibility: String? = "",
        val weight: String? = ""
    )

    data class InfoBanner(
        val banner_id: String? = "",
        val content: String? = "",
        val created_at: String? = "",
        val image: String? = "",
        val name: String? = "",
        val newtab: String? = "",
        val position: String? = "",
        val status: String? = "",
        val title: String? = "",
        val type: String? = "",
        val updated_at: String? = "",
        val url_banner: String? = ""
    )

    data class SliderBanner(
        val banner_id: String? = "",
        val content: String? = "",
        val created_at: String? = "",
        val image: String? = "",
        val name: String? = "",
        val newtab: String? = "",
        val position: String? = "",
        val status: String? = "",
        val title: String? = "",
        val type: String? = "",
        val updated_at: String? = "",
        val url_banner: String? = ""
    )
}