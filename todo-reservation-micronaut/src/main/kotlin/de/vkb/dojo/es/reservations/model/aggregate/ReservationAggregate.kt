package de.vkb.dojo.es.reservations.model.aggregate

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.facilityManagement.model.state.Room
import de.vkb.dojo.es.humanResources.model.state.Person
import de.vkb.dojo.es.reservations.model.state.Reservation

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReservationAggregate(
    val id: String,
    val reservation: Reservation
)
