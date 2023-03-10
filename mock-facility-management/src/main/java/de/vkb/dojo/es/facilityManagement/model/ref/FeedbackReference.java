package de.vkb.dojo.es.facilityManagement.model.ref;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("feedback")
public class FeedbackReference implements Reference {
    private final String id;

    @JsonCreator
    public FeedbackReference(
            @JsonProperty("id") String id
    ) {
        this.id = id;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    @Override
    public String getPath() {
        return "/feedback/" + id;
    }
}
