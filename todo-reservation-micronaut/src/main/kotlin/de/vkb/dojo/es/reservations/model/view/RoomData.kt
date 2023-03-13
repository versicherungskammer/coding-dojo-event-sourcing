package de.vkb.dojo.es.reservations.model.view

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.facilityManagement.model.state.Room

@JsonIgnoreProperties(ignoreUnknown = true)
data class RoomData(
    val id: String,
    val name: String
)

fun RoomData(id: String, room: Room): RoomData = RoomData(id, room.name)
