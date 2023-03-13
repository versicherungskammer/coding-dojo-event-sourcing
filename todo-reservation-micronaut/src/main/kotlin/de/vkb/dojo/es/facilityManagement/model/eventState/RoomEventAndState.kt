package de.vkb.dojo.es.facilityManagement.model.eventState

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent
import de.vkb.dojo.es.facilityManagement.model.state.Room

@JsonIgnoreProperties(ignoreUnknown = true)
data class RoomEventAndState(
    val event: RoomEvent,
    val room: Room?
)
