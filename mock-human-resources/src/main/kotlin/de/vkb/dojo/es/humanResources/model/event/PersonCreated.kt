package de.vkb.dojo.es.humanResources.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-created")
data class PersonCreated(
    override val operationId: String,
    override val aggregateId: String,
    val username: String,
    val fullname: String
): PersonEvent
