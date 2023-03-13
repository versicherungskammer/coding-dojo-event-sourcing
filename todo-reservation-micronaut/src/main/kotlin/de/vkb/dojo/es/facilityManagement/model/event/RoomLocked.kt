package de.vkb.dojo.es.facilityManagement.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room-locked")
data class RoomLocked(
    override val operationId: String,
    override val aggregateId: String
): RoomEvent
