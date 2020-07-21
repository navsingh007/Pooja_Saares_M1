package com.seasia.poojasarees.model.response.category

data class CategoryListOut(
    val children_data: ArrayList<ChildrenData>? = arrayListOf(),
    val id: Int? = 0,
    val is_active: Boolean? = false,
    val level: Int? = 0,
    val name: String? = "",
    val parent_id: Int? = 0,
    val position: Int? = 0,
    val product_count: Int? = 0
) {

    data class ChildrenData(
        val children_data: ArrayList<Any>? = arrayListOf(),
        val id: Int? = 0,
        val is_active: Boolean? = false,
        val level: Int? = 0,
        val name: String? = "",
        val parent_id: Int? = 0,
        val position: Int? = 0,
        val product_count: Int? = 0
    )
}