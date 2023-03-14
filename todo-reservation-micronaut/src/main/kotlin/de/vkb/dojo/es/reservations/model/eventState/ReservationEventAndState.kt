package de.vkb.dojo.es.reservations.model.eventState

import de.vkb.dojo.es.reservations.model.event.ReservationEvent
import de.vkb.dojo.es.reservations.model.state.Reservation

data class ReservationEventAndState(
    val event: ReservationEvent,
    val state: Reservation?
)
