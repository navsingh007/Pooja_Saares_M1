package com.seasia.poojasarees.model.response.shipping

data class SetShippingAndBillingAddressOut(
    val payment_methods: List<PaymentMethod> = listOf(),
    val totals: Totals = Totals()
)

data class PaymentMethod(
    val code: String = "",
    val title: String = ""
)

data class Totals(
    val base_currency_code: String = "",
    val base_discount_amount: Int = 0,
    val base_grand_total: Int = 0,
    val base_shipping_amount: Int = 0,
    val base_shipping_discount_amount: Int = 0,
    val base_shipping_incl_tax: Int = 0,
    val base_shipping_tax_amount: Int = 0,
    val base_subtotal: Int = 0,
    val base_subtotal_with_discount: Int = 0,
    val base_tax_amount: Int = 0,
    val discount_amount: Int = 0,
    val grand_total: Int = 0,
    val items: List<Item> = listOf(),
    val items_qty: Int = 0,
    val quote_currency_code: String = "",
    val shipping_amount: Int = 0,
    val shipping_discount_amount: Int = 0,
    val shipping_incl_tax: Int = 0,
    val shipping_tax_amount: Int = 0,
    val subtotal: Int = 0,
    val subtotal_incl_tax: Int = 0,
    val subtotal_with_discount: Int = 0,
    val tax_amount: Int = 0,
    val total_segments: List<TotalSegment> = listOf(),
    val weee_tax_applied_amount: Any = Any()
)

data class Item(
    val base_discount_amount: Int = 0,
    val base_price: Int = 0,
    val base_price_incl_tax: Int = 0,
    val base_row_total: Int = 0,
    val base_row_total_incl_tax: Int = 0,
    val base_tax_amount: Int = 0,
    val discount_amount: Int = 0,
    val discount_percent: Int = 0,
    val item_id: Int = 0,
    val name: String = "",
    val options: String = "",
    val price: Int = 0,
    val price_incl_tax: Int = 0,
    val qty: Int = 0,
    val row_total: Int = 0,
    val row_total_incl_tax: Int = 0,
    val row_total_with_discount: Int = 0,
    val tax_amount: Int = 0,
    val tax_percent: Int = 0,
    val weee_tax_applied: Any = Any(),
    val weee_tax_applied_amount: Any = Any()
)

data class TotalSegment(
    val area: String = "",
    val code: String = "",
    val extension_attributes: ExtensionAttributes = ExtensionAttributes(),
    val title: String = "",
    val value: Int = 0
)

data class ExtensionAttributes(
    val tax_grandtotal_details: List<Any> = listOf()
)