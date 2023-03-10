package de.vkb.dojo.es.facilityManagement.model.command;

import com.fasterxml.jackson.annotation.*;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("create-room")
public class CreateRoom extends RoomCommand {
    private final String operationId;

    private final String name;

    @JsonCreator
    public CreateRoom(
            @JsonProperty("operationId") String operationId,
            @JsonProperty("name") String name
    ) {
        this.operationId = operationId;
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
        return UUID.randomUUID().toString();
    }
}
