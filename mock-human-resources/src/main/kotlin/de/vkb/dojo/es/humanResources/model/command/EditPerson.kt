package de.vkb.dojo.es.humanResources.model.command

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import de.vkb.dojo.es.humanResources.model.Change

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("edit-person")
data class EditPerson(
    override val operationId: String,
    override val aggregateId: String,
    val username: Change<String>?,
    val fullname: Change<String>?
): PersonCommand
