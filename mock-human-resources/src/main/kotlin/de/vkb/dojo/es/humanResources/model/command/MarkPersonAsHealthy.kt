package de.vkb.dojo.es.humanResources.model.command

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("mark-person-healthy")
data class MarkPersonAsHealthy(
    override val operationId: String,
    override val aggregateId: String
): PersonCommand
