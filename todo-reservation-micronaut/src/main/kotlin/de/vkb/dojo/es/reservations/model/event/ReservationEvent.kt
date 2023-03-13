package de.vkb.dojo.es.reservations.model.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.vkb.dojo.es.common.model.event.Event
import de.vkb.dojo.es.reservations.model.ref.ReservationReference

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ReservationCreated::class, name = "reservation-created"),
    JsonSubTypes.Type(value = ReservationDeleted::class, name = "reservation-deleted")
)
interface ReservationEvent: Event {
    val reference get() = ReservationReference(aggregateId)
}
