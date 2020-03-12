package com.flexe.outbound.model

data class Shipment(
        val id: String?,
        val externalId: String,
        val items: List<Item> = mutableListOf()) {

    data class Item(val sku: String, val quantity: Int)

    fun addItem(item: Item) = (items as MutableList).add(item)
}