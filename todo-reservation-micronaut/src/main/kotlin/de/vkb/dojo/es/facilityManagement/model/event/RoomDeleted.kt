package de.vkb.dojo.es.facilityManagement.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room-deleted")
data class RoomDeleted(
    override val operationId: String,
    override val aggregateId: String
): RoomEvent
