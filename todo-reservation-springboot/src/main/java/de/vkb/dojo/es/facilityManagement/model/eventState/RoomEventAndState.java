package de.vkb.dojo.es.facilityManagement.model.eventState;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.state.Room;

public class RoomEventAndState {
    private final RoomEvent event;
    private final Room state;

    @JsonCreator
    public RoomEventAndState(
            @JsonProperty("event") RoomEvent event,
            @JsonProperty("state") Room state
    ) {
        this.event = event;
        this.state = state;
    }

    @JsonGetter
    public RoomEvent getEvent() {
        return event;
    }

    @JsonGetter
    public Room getState() {
        return state;
    }
}
