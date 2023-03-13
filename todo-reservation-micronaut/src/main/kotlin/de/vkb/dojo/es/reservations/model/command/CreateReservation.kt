package de.vkb.dojo.es.reservations.model.command

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("create-reservation")
data class CreateReservation(
    override val operationId: String,
    val room: String = "",
    val person: String = ""
): ReservationCommand {
    override val aggregateId = UUID.randomUUID().toString()
}
