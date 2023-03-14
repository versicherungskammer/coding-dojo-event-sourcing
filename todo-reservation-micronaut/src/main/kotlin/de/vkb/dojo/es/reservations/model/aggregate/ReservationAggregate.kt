package de.vkb.dojo.es.reservations.model.aggregate

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.reservations.model.state.Reservation

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReservationAggregate(
    val id: String,
    val reservation: Reservation
)
