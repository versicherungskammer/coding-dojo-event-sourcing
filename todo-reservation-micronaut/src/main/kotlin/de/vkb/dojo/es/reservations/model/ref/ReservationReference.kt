package de.vkb.dojo.es.reservations.model.ref

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import de.vkb.dojo.es.common.model.ref.Reference

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("reservation")
data class ReservationReference(
    override val id: String
): Reference {
    override val path: String = "/reservations/${id}"
}
