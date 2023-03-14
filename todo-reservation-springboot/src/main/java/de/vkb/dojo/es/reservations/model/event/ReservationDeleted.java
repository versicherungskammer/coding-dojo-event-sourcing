package de.vkb.dojo.es.reservations.model.event;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("reservatipn-deleted")
public class ReservationDeleted extends ReservationEvent {
    private final String operationId;
    private final String aggregateId;

    @JsonCreator
    public ReservationDeleted(
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
