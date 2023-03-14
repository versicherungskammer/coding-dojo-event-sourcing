package de.vkb.dojo.es.reservations.model.view

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.humanResources.model.state.Person

@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonData(
    val id: String,
    val username: String,
    val fullname: String
)

fun PersonData(id: String, person: Person): PersonData = PersonData(id, person.username, person.fullname)
