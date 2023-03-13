package de.vkb.dojo.es.humanResources.model.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.vkb.dojo.es.common.model.event.Event

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes(
    JsonSubTypes.Type(value = PersonCreated::class, name = "person-created"),
    JsonSubTypes.Type(value = PersonUpdated::class, name = "person-updated"),
    JsonSubTypes.Type(value = PersonSick::class, name = "person-sick"),
    JsonSubTypes.Type(value = PersonHealthy::class, name = "person-healthy"),
    JsonSubTypes.Type(value = PersonDeleted::class, name = "person-deleted")
)
interface PersonEvent: Event
