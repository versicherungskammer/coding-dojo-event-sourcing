package de.vkb.dojo.es.humanResources.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-deleted")
data class PersonDeleted(
    override val operationId: String,
    override val aggregateId: String
): PersonEvent
