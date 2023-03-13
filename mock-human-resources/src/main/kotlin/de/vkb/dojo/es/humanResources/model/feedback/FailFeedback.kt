package de.vkb.dojo.es.humanResources.model.feedback

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("error")
data class FailFeedback(
    val error: String
): Feedback {
    override val success = false
}
