package de.vkb.dojo.es.humanResources.model.eventState

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.state.Person

@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonEventAndState(
    val event: PersonEvent,
    val person: Person?
)
