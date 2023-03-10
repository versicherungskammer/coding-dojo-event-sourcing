package de.vkb.dojo.es.facilityManagement.model.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.facilityManagement.model.state.Room;
import de.vkb.dojo.es.facilityManagement.model.state.builder.RoomBuilder;

public class RoomAggregate {
    private final String id;
    private final Room room;

    @JsonCreator
    public RoomAggregate(
            @JsonProperty("id") String id,
            @JsonProperty("room") Room room
    ) {
        this.id = id;
        this.room = room;
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }

    @JsonGetter("room")
    public Room getRoom() {
        return room;
    }

    public RoomAggregate modify(ModifyOperator change) {
        return new RoomAggregate(
                id,
                change.op(new RoomBuilder(room)).build()
        );
    }

    public interface ModifyOperator {
        RoomBuilder op(RoomBuilder builder);
    }
}
