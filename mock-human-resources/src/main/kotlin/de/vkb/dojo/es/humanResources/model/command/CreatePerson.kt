package de.vkb.dojo.es.humanResources.model.command

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("create-person")
data class CreatePerson(
    override val operationId: String,
    val username: String,
    val fullname: String
): PersonCommand {
    override val aggregateId = UUID.randomUUID().toString()
}
