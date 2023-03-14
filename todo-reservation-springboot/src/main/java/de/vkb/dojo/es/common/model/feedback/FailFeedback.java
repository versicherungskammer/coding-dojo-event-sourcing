package de.vkb.dojo.es.common.model.feedback;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("error")
public class FailFeedback implements Feedback {

    final String error;


    @JsonCreator
    public FailFeedback(
            @JsonProperty("error") String error
    ) {
        if (error == null || error.isBlank()) {
            throw new IllegalArgumentException("need to provide a valid error message");
        }
        this.error = error;
    }

    @JsonGetter
    public String getError() {
        return error;
    }

    public Boolean getSuccess() {
        return false;
    }
}
