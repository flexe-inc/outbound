package com.flexe.outbound.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JsonIgnoreProperties(value = ["aggregateId", "actor", "createdAt", "json"])
interface Event {
    var aggregateId: String
    var actor: String
    val createdAt: String
        get() = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
    val json: String
        get() = objectMapper.writeValueAsString(this)

    companion object {

        fun fromJson(type: String, aggregateId: String, actor: String, json: String): Event {
            val fullType = Class.forName("com.flexe.outbound.event.$type")
            return (objectMapper.readValue(json, fullType) as Event).apply {
                this.aggregateId = aggregateId
                this.actor = actor
            }
        }

        val objectMapper = jacksonObjectMapper()
    }
}

data class ShipmentCreated(val externalId: String): Event {
    override var aggregateId: String = ""
    override var actor: String = ""
    constructor(aggregateId: String, actor: String, externalId: String): this(externalId) {
        this.aggregateId = aggregateId
        this.actor = actor
    }
}

data class ItemAddedToShipment(val sku: String, val quantity: Int): Event {
    override var aggregateId: String = ""
    override var actor: String = ""
    constructor(aggregateId: String, actor: String, sku: String, quantity: Int): this(sku, quantity) {
        this.aggregateId = aggregateId
        this.actor = actor
    }
}

fun main() {
    val evIn = ShipmentCreated(
            aggregateId = "dd1d99ac-632e-11ea-b377-7771fe21259e",
            actor = "me-o",
            externalId = "db3d72b6-632d-11ea-a241-1305ed57a93d")

    val json = evIn.json
    println(json)

    val evOut = Event.fromJson("ShipmentCreated", "me", evIn.aggregateId, json)
    println(evOut)
    println(evOut.createdAt)
}

