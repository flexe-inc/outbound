package com.flexe.outbound.read.controller

import com.flexe.outbound.event.EventStore.aggregate
import com.flexe.outbound.model.Shipment
import com.flexe.outbound.read.build.ShipmentBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("shipment")
class ShipmentController {

    @GetMapping("/{id}")
    fun readShipment(@PathVariable("id") id: String): Shipment =  aggregate(id, ShipmentBuilder())
}