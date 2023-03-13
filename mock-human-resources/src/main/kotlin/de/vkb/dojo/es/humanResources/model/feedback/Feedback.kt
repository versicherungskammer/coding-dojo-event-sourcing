package de.vkb.dojo.es.humanResources.model.feedback

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = UnknownFeedback::class, name = "unknown"),
    JsonSubTypes.Type(value = FailFeedback::class, name = "error"),
    JsonSubTypes.Type(value = SuccessFeedback::class, name = "success")
)
interface Feedback {
    val success: Boolean
}
