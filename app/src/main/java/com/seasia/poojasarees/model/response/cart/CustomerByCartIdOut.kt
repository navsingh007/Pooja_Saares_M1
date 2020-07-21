package com.seasia.poojasarees.model.response.cart

data class CustomerByCartIdOut(
    val billing_address: BillingAddress? = BillingAddress(),
    val created_at: String? = "",
    val currency: Currency? = Currency(),
    val customer: Customer? = Customer(),
    val customer_is_guest: Boolean? = false,
    val customer_note_notify: Boolean? = false,
    val customer_tax_class_id: Int? = 0,
    val extension_attributes: ExtensionAttributesShipping? = ExtensionAttributesShipping(),
    val id: Int? = 0,
    val is_active: Boolean? = false,
    val is_virtual: Boolean? = false,
    val items: List<ItemX>? = listOf(),
    val items_count: Int? = 0,
    val items_qty: Int? = 0,
    val orig_order_id: Int? = 0,
    val store_id: Int? = 0,
    val updated_at: String? = ""
) {

    data class BillingAddress(
        val city: Any? = Any(),
        val country_id: Any? = Any(),
        val customer_id: Int? = 0,
        val email: String? = "",
        val firstname: Any? = Any(),
        val id: Int? = 0,
        val lastname: Any? = Any(),
        val postcode: Any? = Any(),
        val region: Any? = Any(),
        val region_code: Any? = Any(),
        val region_id: Any? = Any(),
        val same_as_billing: Int? = 0,
        val save_in_address_book: Int? = 0,
        val street: List<String>? = listOf(),
        val telephone: Any? = Any()
    )

    data class Currency(
        val base_currency_code: String? = "",
        val base_to_global_rate: Int? = 0,
        val base_to_quote_rate: Int? = 0,
        val global_currency_code: String? = "",
        val quote_currency_code: String? = "",
        val store_currency_code: String? = "",
        val store_to_base_rate: Int? = 0,
        val store_to_quote_rate: Int? = 0
    )

    data class Customer(
        val addresses: List<Any>? = listOf(),
        val created_at: String? = "",
        val created_in: String? = "",
        val custom_attributes: List<CustomAttribute>? = listOf(),
        val disable_auto_group_change: Int? = 0,
        val dob: String? = "",
        val email: String? = "",
        val extension_attributes: ExtensionAttributes? = ExtensionAttributes(),
        val firstname: String? = "",
        val gender: Int? = 0,
        val group_id: Int? = 0,
        val id: Int? = 0,
        val lastname: String? = "",
        val store_id: Int? = 0,
        val taxvat: String? = "",
        val updated_at: String? = "",
        val website_id: Int? = 0
    )

    data class ExtensionAttributesShipping(
        val shipping_assignments: List<ShippingAssignment>? = listOf()
    )

    data class ItemX(
        val item_id: Int? = 0,
        val name: String? = "",
        val price: Int? = 0,
        val product_type: String? = "",
        val qty: Int? = 0,
        val quote_id: String? = "",
        val sku: String? = ""
    )

    data class CustomAttribute(
        val attribute_code: String? = "",
        val value: String? = ""
    )

    data class ExtensionAttributes(
        val is_subscribed: Boolean? = false
    )

    data class ShippingAssignment(
        val items: List<Item>? = listOf(),
        val shipping: Shipping? = Shipping()
    )

    data class Item(
        val item_id: Int? = 0,
        val name: String? = "",
        val price: Int? = 0,
        val product_type: String? = "",
        val qty: Int? = 0,
        val quote_id: String? = "",
        val sku: String? = ""
    )

    data class Shipping(
        val address: Address? = Address(),
        val method: Any? = Any()
    )

    data class Address(
        val city: Any? = Any(),
        val country_id: Any? = Any(),
        val customer_id: Int? = 0,
        val email: String? = "",
        val firstname: Any? = Any(),
        val id: Int? = 0,
        val lastname: Any? = Any(),
        val postcode: Any? = Any(),
        val region: Any? = Any(),
        val region_code: Any? = Any(),
        val region_id: Any? = Any(),
        val same_as_billing: Int? = 0,
        val save_in_address_book: Int? = 0,
        val street: List<String>? = listOf(),
        val telephone: Any? = Any()
    )
}