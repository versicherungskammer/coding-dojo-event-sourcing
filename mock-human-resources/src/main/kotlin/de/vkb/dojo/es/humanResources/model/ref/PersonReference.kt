package de.vkb.dojo.es.humanResources.model.ref

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person")
data class PersonReference(
    override val id: String
): Reference {
    override val path: String = "/persons/${id}"
}
