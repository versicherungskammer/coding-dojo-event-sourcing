package de.vkb.dojo.es.reservations.model.command

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("delete-reservation")
data class DeleteReservation(
    override val operationId: String,
    override val aggregateId: String
): ReservationCommand
