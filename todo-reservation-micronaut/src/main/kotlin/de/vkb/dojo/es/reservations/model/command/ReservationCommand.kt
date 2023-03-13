package de.vkb.dojo.es.reservations.model.command

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes(
    JsonSubTypes.Type(value = CreateReservation::class, name = "create-reservation"),
    JsonSubTypes.Type(value = DeleteReservation::class, name = "delete-reservation")
)
interface ReservationCommand: Command
