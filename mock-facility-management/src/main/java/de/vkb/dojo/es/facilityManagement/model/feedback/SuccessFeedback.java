package de.vkb.dojo.es.facilityManagement.model.feedback;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.facilityManagement.model.feedback.ref.Reference;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("success")
public class SuccessFeedback implements Feedback {
    private final Reference reference;

    @JsonCreator
    public SuccessFeedback(
            @JsonProperty("reference") Reference reference
    ) {
        this.reference = reference;
    }

    @JsonGetter
    public Reference getReference() {
        return reference;
    }

    public Boolean getSuccess() {
        return true;
    }
}
