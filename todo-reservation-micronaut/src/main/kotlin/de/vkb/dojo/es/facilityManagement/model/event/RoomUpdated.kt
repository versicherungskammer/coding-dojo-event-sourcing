package de.vkb.dojo.es.facilityManagement.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import de.vkb.dojo.es.common.model.Change

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room-updated")
data class RoomUpdated(
    override val operationId: String,
    override val aggregateId: String,
    val name: Change<String>?
): RoomEvent
