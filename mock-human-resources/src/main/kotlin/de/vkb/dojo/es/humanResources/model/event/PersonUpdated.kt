package de.vkb.dojo.es.humanResources.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import de.vkb.dojo.es.humanResources.model.Change

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-updated")
data class PersonUpdated(
    override val operationId: String,
    override val aggregateId: String,
    val username: Change<String>?,
    val fullname: Change<String>?
): PersonEvent
