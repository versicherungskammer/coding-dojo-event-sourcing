package de.vkb.dojo.es.reservations.model.dto;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.reservations.model.ref.ReservationReference;
import de.vkb.dojo.es.reservations.model.state.Reservation;
import de.vkb.dojo.es.reservations.model.view.PersonData;
import de.vkb.dojo.es.reservations.model.view.RoomData;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationOutput extends Reservation {
    private final String id;
    private final ReservationReference ref;
    private final Map<String,String> links = new HashMap<>();

    @JsonCreator
    public ReservationOutput(
            @JsonProperty("id") String id,
            @JsonProperty("room") RoomData room,
            @JsonProperty("person") PersonData person
    ) {
        super(room, person);
        this.id = id;
        this.ref = new ReservationReference(id);
        this.links.put("self", ref.getPath());
    }

    @JsonCreator
    public ReservationOutput(
            @JsonProperty("id") String id,
            Reservation template
    ) {
        super(template.getRoom(), template.getPerson());
        this.id = id;
        this.ref = new ReservationReference(id);
        this.links.put("self", ref.getPath());
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }

    @JsonGetter("_links")
    public Map<String,String> getLinks() {
        return links;
    }

    @JsonIgnore
    public ReservationReference getRef() {
        return ref;
    }
}
