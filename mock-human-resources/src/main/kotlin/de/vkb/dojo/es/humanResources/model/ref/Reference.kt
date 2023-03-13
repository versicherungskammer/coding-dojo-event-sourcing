package de.vkb.dojo.es.humanResources.model.ref

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = PersonReference::class, name = "person"),
    JsonSubTypes.Type(value = FeedbackReference::class, name = "feedback")
)
interface Reference {
    val id: String
    val path: String
}
