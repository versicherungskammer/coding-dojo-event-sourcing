package de.vkb.dojo.es.common.model.ref

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.vkb.dojo.es.reservations.model.ref.ReservationReference

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ReservationReference::class, name = "reservation"),
    JsonSubTypes.Type(value = FeedbackReference::class, name = "feedback")
)
interface Reference {
    val id: String
    val path: String
}
