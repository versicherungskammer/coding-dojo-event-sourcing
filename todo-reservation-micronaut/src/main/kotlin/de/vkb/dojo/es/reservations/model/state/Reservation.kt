package de.vkb.dojo.es.reservations.model.state

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.reservations.model.view.PersonData
import de.vkb.dojo.es.reservations.model.view.RoomData

@JsonIgnoreProperties(ignoreUnknown = true)
data class Reservation(
    val person: PersonData,
    val room: RoomData
)
