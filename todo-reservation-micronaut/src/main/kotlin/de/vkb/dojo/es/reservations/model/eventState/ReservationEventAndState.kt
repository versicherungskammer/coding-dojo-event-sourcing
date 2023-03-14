package de.vkb.dojo.es.reservations.model.eventState

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.reservations.model.event.ReservationEvent
import de.vkb.dojo.es.reservations.model.state.Reservation

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReservationEventAndState(
    val event: ReservationEvent,
    val state: Reservation?
)
