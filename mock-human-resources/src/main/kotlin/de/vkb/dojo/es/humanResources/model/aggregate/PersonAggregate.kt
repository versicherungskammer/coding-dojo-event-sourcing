package de.vkb.dojo.es.humanResources.model.aggregate

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.humanResources.model.state.Person

@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonAggregate(
    val id: String,
    val person: Person
)
