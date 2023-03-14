package de.vkb.dojo.es.reservations.model.event;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.reservations.model.view.PersonData;
import de.vkb.dojo.es.reservations.model.view.RoomData;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("reservatipn-created")
public class ReservationCreated extends ReservationEvent {
    private final String operationId;
    private final String aggregateId;

    private final RoomData room;

    private final PersonData person;
    @JsonCreator
    public ReservationCreated(
            @JsonProperty("operationId") String operationId,
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("room") RoomData room,
            @JsonProperty("person") PersonData person
    ) {
        this.operationId = operationId;
        this.aggregateId = aggregateId;
        this.room = room;
        this.person = person;
    }

    @Override
    @JsonGetter
    public String getOperationId() {
        return operationId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
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
