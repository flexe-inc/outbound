package com.flexe.outbound.command

import com.flexe.outbound.event.Event
import java.util.*

abstract class Command(val aggregateId: String, val correlationId: String, val actor: String) {
    abstract fun perform(existingEvents: List<Event>): List<Event>
}