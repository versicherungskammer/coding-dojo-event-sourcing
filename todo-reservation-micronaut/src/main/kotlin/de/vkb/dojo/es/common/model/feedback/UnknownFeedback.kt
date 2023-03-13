package de.vkb.dojo.es.common.model.feedback

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("unknown")
class UnknownFeedback: Feedback {
    override val success = false
    val message = "retry in a few seconds"
}

