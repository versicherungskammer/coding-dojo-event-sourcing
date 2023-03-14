package de.vkb.dojo.es.facilityManagement.model.event;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room-unlocked")
public class RoomUnlocked extends RoomEvent {
    private final String operationId;
    private final String aggregateId;

    @JsonCreator
    public RoomUnlocked(
            @JsonProperty("operatonId") String operationId,
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
