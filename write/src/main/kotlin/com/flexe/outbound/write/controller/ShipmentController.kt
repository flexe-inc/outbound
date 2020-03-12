package com.flexe.outbound.write.controller

import com.flexe.outbound.write.command.CreateShipment
import com.flexe.outbound.event.EventStore.processCommand
import com.flexe.outbound.model.Shipment
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

@RestController("shipment")
class ShipmentController {

    private val counter = AtomicLong(1)

    @PostMapping("/")
    fun create(
            @RequestBody shipment: Shipment,
            @RequestHeader("X-FLEXE-correlationId") correlationId: String,
            @RequestHeader("X-FLEXE-email") actor: String
    ): Shipment {
        val id = UUID.randomUUID().toString()
        val shipmentWithId = Shipment(id, shipment.externalId, shipment.items)

        val command = CreateShipment(shipmentWithId, correlationId, actor)
        processCommand(command)

        return shipmentWithId
    }
}
