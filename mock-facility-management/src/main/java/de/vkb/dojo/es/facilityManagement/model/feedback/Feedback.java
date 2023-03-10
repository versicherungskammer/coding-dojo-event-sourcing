package de.vkb.dojo.es.facilityManagement.model.feedback;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FailFeedback.class, name = "error"),
        @JsonSubTypes.Type(value = SuccessFeedback.class, name = "success")
})
public interface Feedback {
    Boolean getSuccess();
}
