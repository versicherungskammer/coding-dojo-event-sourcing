package de.vkb.dojo.es.humanResources.model.command

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.vkb.dojo.es.humanResources.model.ref.PersonReference

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes(
    JsonSubTypes.Type(value = CreatePerson::class, name = "create-person"),
    JsonSubTypes.Type(value = EditPerson::class, name = "edit-person"),
    JsonSubTypes.Type(value = MarkPersonAsSick::class, name = "mark-person-sick"),
    JsonSubTypes.Type(value = MarkPersonAsHealthy::class, name = "mark-person-healthy"),
    JsonSubTypes.Type(value = DeletePerson::class, name = "delete-person")
)
interface PersonCommand: Command
