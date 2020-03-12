package com.flexe.outbound.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.flexe.outbound.build.AggregateBuilder
import com.flexe.outbound.command.Command
import com.github.msemys.esjc.EventData
import com.github.msemys.esjc.EventStoreBuilder
import com.github.msemys.esjc.ExpectedVersion
import java.util.*

interface Store {
    fun store(correlationId: String, events: List<Event>)
    fun events(id: String): List<Event>
    fun processCommand(command: Command)
    fun <T> aggregate(id: String, builder: AggregateBuilder<T>): T
}

object EventStore: Store {

    private val host = System.getenv("STORE_HOST") ?: "127.0.0.1"
    private val port = System.getenv("STORE_PORT")?.toIntOrNull() ?: 1113
    private val metadataMapper = ObjectMapper()

    private val store = EventStoreBuilder
            .newBuilder()
            .singleNodeAddress(host, port)
            .connectionName("Outbound Writer")
            .build()

    override fun processCommand(command: Command) =
            store(command.correlationId, command.perform(events(command.aggregateId)))

    override fun <T> aggregate(id: String, builder: AggregateBuilder<T>): T =
            builder.buildAggregate(events(id))

    override fun store(correlationId: String, events: List<Event>) {
        events.groupBy { it.aggregateId }.forEach { (aggregateId, events) ->
            store.appendToStream(aggregateId, ExpectedVersion.ANY, events.map {
                EventData
                        .newBuilder()
                        .type(it::class.java.simpleName)
                        .metadata(metadata(correlationId, it))
                        .jsonData(it.json)
                        .build() })
                    .join()
        }
    }

    private fun metadata(correlationId: String, event: Event): String = metadataMapper.writeValueAsString(mapOf(
            "correlationId" to correlationId.toString(),
            "createdAt" to event.createdAt,
            "actor" to event.actor))

    override fun events(id: String): List<Event> {
        val events = mutableListOf<Event>()
        store.readStreamEventsForward(id, 0, 4096, false)
            .thenAccept { slice ->
                slice.events.forEach { result ->
                    val event = result.event
                    events.add(Event.fromJson(
                        event.eventType,
                        event.eventStreamId,
                        "me",
                        String(event.data)))

                }
            }
            .join()
        return events
    }
}

