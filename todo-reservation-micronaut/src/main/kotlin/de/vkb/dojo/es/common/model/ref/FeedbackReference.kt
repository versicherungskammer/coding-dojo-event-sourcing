package de.vkb.dojo.es.common.model.ref

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("feedback")
data class FeedbackReference(
    override val id: String
): Reference {
    override val path: String = "/feedback/${id}"
}
