package de.vkb.dojo.es.reservations.model.eventState;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.reservations.model.event.ReservationEvent;
import de.vkb.dojo.es.reservations.model.state.Reservation;

public class ReservationEventAndState {
    private final ReservationEvent event;
    private final Reservation state;

    @JsonCreator
    public ReservationEventAndState(
            @JsonProperty("event") ReservationEvent event,
            @JsonProperty("state") Reservation state
    ) {
        this.event = event;
        this.state = state;
    }

    @JsonGetter
    public ReservationEvent getEvent() {
        return event;
    }

    @JsonGetter
    public Reservation getState() {
        return state;
    }
}
