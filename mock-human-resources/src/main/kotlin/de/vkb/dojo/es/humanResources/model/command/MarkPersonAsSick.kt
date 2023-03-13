package de.vkb.dojo.es.humanResources.model.command

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("mark-person-sick")
data class MarkPersonAsSick(
    override val operationId: String,
    override val aggregateId: String
): PersonCommand
