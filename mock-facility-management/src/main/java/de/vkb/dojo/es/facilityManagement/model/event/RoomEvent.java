package de.vkb.dojo.es.facilityManagement.model.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.vkb.dojo.es.facilityManagement.model.ref.Reference;
import de.vkb.dojo.es.facilityManagement.model.ref.RoomReference;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomCreated.class, name = "room-created"),
        @JsonSubTypes.Type(value = RoomUpdated.class, name = "room-updated"),
        @JsonSubTypes.Type(value = RoomLocked.class, name = "room-locked"),
        @JsonSubTypes.Type(value = RoomUnlocked.class, name = "room-unlocked"),
        @JsonSubTypes.Type(value = RoomDeleted.class, name = "room-deleted")
})
public abstract class RoomEvent implements Event {
    public abstract String getOperationId();
    public abstract String getAggregateId();
    @Override
    public Reference getReference() {
        return new RoomReference(getAggregateId());
    }
}
