package de.vkb.dojo.es.reservations.model.command;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("delete-reservation")
public class DeleteReservation extends ReservationCommand {
    private final String operationId;
    private final String aggregateId;

    @JsonCreator
    public DeleteReservation(
            @JsonProperty("operationId") String operationId,
            @JsonProperty("aggregateId") String aggregateId
    ) {
        this.operationId = operationId;
        this.aggregateId = aggregateId;
    }

    @Override
    @JsonGetter
    public String getOperationId() {
        return operationId;
    }

    @Override
    @JsonGetter
    public String getAggregateId() {
        return aggregateId;
    }
}
