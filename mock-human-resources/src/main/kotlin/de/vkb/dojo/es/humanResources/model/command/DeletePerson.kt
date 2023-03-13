package de.vkb.dojo.es.humanResources.model.command

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("delete-person")
data class DeletePerson(
    override val operationId: String,
    override val aggregateId: String
): PersonCommand
