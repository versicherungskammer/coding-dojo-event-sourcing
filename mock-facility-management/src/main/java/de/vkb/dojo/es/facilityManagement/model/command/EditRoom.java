package de.vkb.dojo.es.facilityManagement.model.command;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.facilityManagement.model.Change;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("edit-room")
public class EditRoom extends RoomCommand {
    private final String operationId;
    private final String aggregateId;

    private final Change<String> name;

    @JsonCreator
    public EditRoom(
            @JsonProperty("operationId") String operationId,
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

    @JsonGetter
    public Change<String> getName() {
        return name;
    }

    @Override
    @JsonGetter
    public String getAggregateId() {
        return aggregateId;
    }
}
