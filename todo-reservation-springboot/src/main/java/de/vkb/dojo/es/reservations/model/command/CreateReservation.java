package de.vkb.dojo.es.reservations.model.command;

import com.fasterxml.jackson.annotation.*;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("create-reservation")
public class CreateReservation extends ReservationCommand {
    private final String operationId;

    private final String room;
    private final String person;

    @JsonCreator
    public CreateReservation(
            @JsonProperty("operationId") String operationId,
            @JsonProperty("room") String room,
            @JsonProperty("person") String person
    ) {
        this.operationId = operationId;
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
        return UUID.randomUUID().toString();
    }

    @JsonGetter
    public String getRoom() {
        return room;
    }

    @JsonGetter
    public String getPerson() {
        return person;
    }
}
