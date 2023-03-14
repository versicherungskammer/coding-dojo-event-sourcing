package de.vkb.dojo.es.reservations.model.ref;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.common.model.ref.Reference;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("reservation")
public class ReservationReference implements Reference {
    private final String id;

    @JsonCreator
    public ReservationReference(
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
        return "/reservations/" + id;
    }
}
