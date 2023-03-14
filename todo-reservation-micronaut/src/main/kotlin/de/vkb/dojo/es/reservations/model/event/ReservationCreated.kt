package de.vkb.dojo.es.reservations.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import de.vkb.dojo.es.reservations.model.view.PersonData
import de.vkb.dojo.es.reservations.model.view.RoomData

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("reservation-created")
data class ReservationCreated(
    override val operationId: String,
    override val aggregateId: String,
    val room: RoomData,
    val person: PersonData
): ReservationEvent
