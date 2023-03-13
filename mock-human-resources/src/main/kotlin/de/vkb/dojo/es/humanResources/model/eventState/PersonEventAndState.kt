package de.vkb.dojo.es.humanResources.model.eventState

import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.state.Person

data class PersonEventAndState(
    val event: PersonEvent,
    val person: Person?
)
