package com.flexe.outbound.write.command

import com.flexe.outbound.event.ItemAddedToShipment
import com.flexe.outbound.event.ShipmentCreated
import com.flexe.outbound.model.Shipment
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

class CreateShipmentTest {

    val shipmentId = UUID.randomUUID().toString()
    val externalId = UUID.randomUUID().toString()
    val sku = "ONE"
    val quantity = 13
    val items = listOf(Shipment.Item(sku, quantity))
    val actor = "user@flexe.com"
    val correlationId = UUID.randomUUID().toString()

    @Test
    fun `should create a shipment-created and to item-added-to-shipment events`() {

        val shipment = Shipment(
                id = shipmentId,
                externalId = externalId,
                items = items)

        val createShipment = CreateShipment(shipment, correlationId, actor)

        val events = createShipment.perform(emptyList())

        assertThat(events, hasSize(2))
        assertThat(events[0] as ShipmentCreated, equalTo(ShipmentCreated(shipmentId, actor, externalId)))
        assertThat(events[1] as ItemAddedToShipment, equalTo(ItemAddedToShipment(sku, quantity)))
    }

    @Test
    fun `should not create events if the shipment already exists`() {

        val shipment = Shipment(
                id = shipmentId,
                externalId = externalId,
                items = items)
        val existingEvent = ShipmentCreated("", "", "")

        val createShipment = CreateShipment(shipment, correlationId, actor)

        val events = createShipment.perform(listOf(existingEvent))

        assertThat(events, hasSize(0))
    }
}
