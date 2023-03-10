package de.vkb.dojo.es.facilityManagement.model.command;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("lock-room")
public class LockRoom extends RoomCommand {
    private final String operationId;
    private final String aggregateId;

    @JsonCreator
    public LockRoom(
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
