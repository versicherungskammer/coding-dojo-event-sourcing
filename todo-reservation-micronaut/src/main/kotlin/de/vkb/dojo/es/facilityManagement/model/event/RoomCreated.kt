package de.vkb.dojo.es.facilityManagement.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room-created")
data class RoomCreated(
    override val operationId: String,
    override val aggregateId: String,
    val name: String,
): RoomEvent
