package de.vkb.dojo.es.reservations.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.reservations.model.ref.ReservationReference
import de.vkb.dojo.es.reservations.model.state.Reservation
import de.vkb.dojo.es.reservations.model.view.PersonData
import de.vkb.dojo.es.reservations.model.view.RoomData

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReservationOutput(
    val id: String,
    val person: PersonData,
    val room: RoomData
) {
    val links = mapOf(
        "self" to ReservationReference(id).path
    )
}

fun ReservationOutput(id: String, reservation: Reservation): ReservationOutput = ReservationOutput(id, reservation.person, reservation.room)
