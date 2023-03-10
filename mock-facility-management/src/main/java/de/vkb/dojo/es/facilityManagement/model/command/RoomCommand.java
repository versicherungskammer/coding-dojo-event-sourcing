package de.vkb.dojo.es.facilityManagement.model.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateRoom.class, name = "create-room"),
        @JsonSubTypes.Type(value = EditRoom.class, name = "edit-room"),
        @JsonSubTypes.Type(value = LockRoom.class, name = "lock-room"),
        @JsonSubTypes.Type(value = UnlockRoom.class, name = "unlock-room"),
        @JsonSubTypes.Type(value = DeleteRoom.class, name = "delete-room")
})
public abstract class RoomCommand implements Command {
    public abstract String getOperationId();
    public abstract String getAggregateId();
}
