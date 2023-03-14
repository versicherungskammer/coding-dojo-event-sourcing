package de.vkb.dojo.es.facilityManagement.model.event;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.common.model.Change;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("room-updated")
public class RoomUpdated extends RoomEvent {
    private final String operationId;
    private final String aggregateId;

    private final Change<String> name;

    @JsonCreator
    public RoomUpdated(
            @JsonProperty("operatonId") String operationId,
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("name") Change<String> name
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

    @Override
    @JsonGetter
    public String getAggregateId() {
        return aggregateId;
    }

    @JsonGetter
    public Change<String> getName() {
        return name;
    }
}
