package de.vkb.dojo.es.humanResources.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-sick")
data class PersonSick(
    override val operationId: String,
    override val aggregateId: String
): PersonEvent
