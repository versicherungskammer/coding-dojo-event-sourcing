package de.vkb.dojo.es.facilityManagement.model.event;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room-created")
public class RoomCreated extends RoomEvent {
    private final String operationId;
    private final String aggregateId;

    private final String name;

    @JsonCreator
    public RoomCreated(
            @JsonProperty("operationId") String operationId,
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("name") String name
    ) {
        this.operationId = operationId;
        this.aggregateId = aggregateId;
        this.name = name;
    }

    @Override
    @JsonGetter
    public String getOperationId() {
        return operationId;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }
}
