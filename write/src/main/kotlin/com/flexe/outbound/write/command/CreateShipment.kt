package com.flexe.outbound.write.command

import com.flexe.outbound.command.Command
import com.flexe.outbound.event.Event
import com.flexe.outbound.event.ItemAddedToShipment
import com.flexe.outbound.event.ShipmentCreated
import com.flexe.outbound.model.Shipment
import java.util.*

class CreateShipment(
        private val shipment: Shipment,
        correlationId: String,
        actor: String): Command(shipment.id!!, correlationId, actor) {
    override fun perform(existingEvents: List<Event>): List<Event> {

        if (existingEvents.isNotEmpty()) return emptyList()

        val events = mutableListOf<Event>()
        events.add(ShipmentCreated(aggregateId, actor, shipment.externalId))

        events.addAll(shipment.items.map { item ->
            ItemAddedToShipment(aggregateId, actor, item.sku, item.quantity)
        })

        return events
    }
}