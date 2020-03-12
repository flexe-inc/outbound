# Outbound Service Prototype

This project is a prototype for a new outbound shipment workflow based on event sourcing.
It is designed as a proof-of-concept for building a throughput-focused outbound shipment
service for FLEXE.

This prototype uses the following technology:

- Kotlin: https://kotlinlang.org/
- Spring boot: https://spring.io/projects/spring-boot
- Event Store: https://eventstore.com/
- Docker: https://www.docker.com/
- Gradle: https://gradle.org/

## Quickstart

### Prerequisites

- JDK (at least 1.8): https://www.java.com/ES/download/
- docker-compose: https://docs.docker.com/compose/install/

### Start the service

```
> ./stack.sh up
```

### Stop the service

```
> ./stack.sh down
```

### Create a New Shipment

```
curl --header "Content-Type: application/json" \
  --header "X-FLEXE-email:test@flexe.com" \
  --header "X-FLEXE-correlationId:6d3eecc8-64ad-11ea-82ea-f7ad434a87d4" \
  --request POST \
  --data '{ "externalId": "96c0dd72-64ad-11ea-8a4c-5bbc3dab2726", "items": [{ "sku": "SKU_1", "quantity": 1 }, { "sku": "SKU_2", "quantity": 2 }]}' \
  http://localhost:2035/
```

You should receive a json result from the service that contains the id of the new shipment

### View Shipment Details

```
curl http://localhost:2034/<shipment id>
```

This should return the json shipment details for the given shipment

### View Shipments Events

In a browser open: http://localhost:2113/web/index.html#/streams/<shipment id>

This will open the Event Store UI on the given shipment.
In this view you will be able to browse the events that led to the 
current state of the given shipment.

## Service Architecture and Concepts

This application utilises event sourcing to create a simple
service used to create and read shipment objects. In order 
to understand the architecture it is important to understand
the following concepts:

- Event: a single action by a user, stored as a first-class instance in the event store. Events are 
  tied to an aggregate and are ordered chronologically.
- Aggregate: a collection of events all working on the same object with a shared ID.
- Domain Object: an object that belongs to the given domain and is the result of playing 
  through a series of events for a single aggregate.

In an event-sourced application we do not store the current state of every object,
rather we keep a historical record of events that were performed to each aggregate.
We can use these events to determine the current state of a domain object but can also use
them to determine the history of that object and the state at a given time.

### Write Service

This service has an HTTP POST endpoint that accepts a representation of a shipment
and converts it to a set of events for storage in the event store.
More endpoints can be created for managing shipment details and state to flesh out
such a service and get feature parity with warehouser.

### Read Service

This service has an HTTP GET endpoint that returns shipment details for a given aggregate id.
This is achieved by reading the entire stream of events for a shipment and playing them
forward. In addition to this kind of read service it is possible to build more read services
for different needs, e.g.:
- an in-memory cache of shipments that are validated but unbatched that would read in all new
  events and update the cache in real time.
- a set of DB tables that could be accessed by BI teams or reporting tools allowing ad-hoc
  building, also updated by constantly reading a stream of events.

### Event storage

We have used an in-memory event storage solution called Event Store for this prototype. This
is a simple docker instance with no extra configuration and would not be production-suitable but
is useful for this prototype.