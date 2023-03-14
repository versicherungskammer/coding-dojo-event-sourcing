package de.vkb.dojo.es.reservations.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.reservations.model.view.PersonData;
import de.vkb.dojo.es.reservations.model.view.RoomData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation {
    private final RoomData room;
    private final PersonData person;

    @JsonCreator
    public Reservation(
            @JsonProperty("room") RoomData room,
            @JsonProperty("person") PersonData person
    ) {
        this.room = room;
        this.person = person;
    }

    @JsonGetter
    public RoomData getRoom() {
        return room;
    }

    @JsonGetter
    public PersonData getPerson() {
        return person;
    }
}
