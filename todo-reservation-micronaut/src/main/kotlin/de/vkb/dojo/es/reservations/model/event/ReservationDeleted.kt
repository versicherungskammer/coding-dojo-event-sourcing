package de.vkb.dojo.es.reservations.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("reservation-deleted")
data class ReservationDeleted(
    override val operationId: String,
    override val aggregateId: String
): ReservationEvent
