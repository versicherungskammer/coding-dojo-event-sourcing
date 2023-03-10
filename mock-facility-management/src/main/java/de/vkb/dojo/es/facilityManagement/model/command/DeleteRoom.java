package de.vkb.dojo.es.facilityManagement.model.command;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("delete-room")
public class DeleteRoom extends RoomCommand {
    private final String operationId;
    private final String aggregateId;

    @JsonCreator
    public DeleteRoom(
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
