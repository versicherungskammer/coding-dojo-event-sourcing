package de.vkb.dojo.es.facilityManagement.model.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("unknown")
public class UnknownFeedback implements Feedback {
    @JsonCreator
    public UnknownFeedback() {
    }

    @JsonGetter
    public String getMessage() {
        return "retry in a few seconds";
    }

    public Boolean getSuccess() {
        return false;
    }
}
