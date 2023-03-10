package de.vkb.dojo.es.facilityManagement.model.feedback.ref;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room")
public class RoomReference implements Reference {
    private final String id;

    @JsonCreator
    public RoomReference(
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
        return "/rooms/" + id;
    }
}
