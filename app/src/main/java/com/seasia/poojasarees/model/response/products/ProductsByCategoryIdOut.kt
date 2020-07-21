package com.seasia.poojasarees.model.response.products

data class ProductsByCategoryIdOut(
    val items: ArrayList<Item>? = arrayListOf(),
    val search_criteria: SearchCriteria? = SearchCriteria(),
    val total_count: Int? = 0
) {
    data class Item(
        val attribute_set_id: Int? = 0,
        val created_at: String? = "",
        val custom_attributes: ArrayList<CustomAttribute>? = arrayListOf(),
        val extension_attributes: ExtensionAttributes? = ExtensionAttributes(),
        val id: Int? = 0,
        val media_gallery_entries: ArrayList<MediaGalleryEntry>? = arrayListOf(),
        val name: String? = "",
        val options: ArrayList<Any>? = arrayListOf(),
        val price: Int? = 0,
        val product_links: ArrayList<Any>? = arrayListOf(),
        val sku: String? = "",
        val status: Int? = 0,
        val tier_prices: ArrayList<Any>? = arrayListOf(),
        val type_id: String? = "",
        val updated_at: String? = "",
        val visibility: Int? = 0,
        val weight: Int? = 0
    )

    data class SearchCriteria(
        val current_page: Int? = 0,
        val filter_groups: ArrayList<FilterGroup>? = arrayListOf(),
        val page_size: Int? = 0,
        val sort_orders: ArrayList<SortOrder>? = arrayListOf()
    )

    data class CustomAttribute(
        val attribute_code: String? = "",
        val value: Any? = Any()
    )

    data class ExtensionAttributes(
        val category_links: ArrayList<CategoryLink>? = arrayListOf(),
        val configurable_product_links: ArrayList<Int>? = arrayListOf(),
        val configurable_product_options: ArrayList<ConfigurableProductOption>? = arrayListOf(),
        val website_ids: ArrayList<Int>? = arrayListOf()
    )

    data class MediaGalleryEntry(
        val disabled: Boolean? = false,
        val `file`: String? = "",
        val id: Int? = 0,
        val label: Any? = Any(),
        val media_type: String? = "",
        val position: Int? = 0,
        val types: ArrayList<String>? = arrayListOf()
    )

    data class CategoryLink(
        val category_id: String? = "",
        val position: Int? = 0
    )

    data class ConfigurableProductOption(
        val attribute_id: String? = "",
        val id: Int? = 0,
        val label: String? = "",
        val position: Int? = 0,
        val product_id: Int? = 0,
        val values: ArrayList<Value>? = arrayListOf()
    )

    data class Value(
        val value_index: Int? = 0
    )

    data class FilterGroup(
        val filters: ArrayList<Filter>? = arrayListOf()
    )

    data class SortOrder(
        val direction: String? = "",
        val `field`: String? = ""
    )

    data class Filter(
        val condition_type: String? = "",
        val `field`: String? = "",
        val value: String? = ""
    )
}