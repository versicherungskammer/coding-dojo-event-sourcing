package de.vkb.dojo.es.humanResources.model.feedback

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import de.vkb.dojo.es.humanResources.model.ref.Reference

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("success")
data class SuccessFeedback(
    val reference: Reference
): Feedback {
    override val success = true
}

