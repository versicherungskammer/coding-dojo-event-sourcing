package de.vkb.dojo.es.reservations.model.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.reservations.model.state.Reservation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationAggregate {
    private final String id;
    private final Reservation reservation;

    @JsonCreator
    public ReservationAggregate(
            @JsonProperty("id") String id,
            @JsonProperty("reservation") Reservation reservation
    ) {
        this.id = id;
        this.reservation = reservation;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    @JsonGetter
    public Reservation getReservation() {
        return reservation;
    }
}
