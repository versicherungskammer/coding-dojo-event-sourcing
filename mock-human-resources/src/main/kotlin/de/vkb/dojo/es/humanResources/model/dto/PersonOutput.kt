package de.vkb.dojo.es.humanResources.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.humanResources.model.ref.PersonReference
import de.vkb.dojo.es.humanResources.model.state.Person

@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonOutput(
    val id: String,
    val username: String,
    val fullname: String,
    val sick: Boolean
) {
    val links = mapOf(
        "self" to PersonReference(id).path
    )
}

fun PersonOutput(id: String, person: Person): PersonOutput = PersonOutput(id, person.username, person.fullname, person.sick)
