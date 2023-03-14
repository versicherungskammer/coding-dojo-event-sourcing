package de.vkb.dojo.es.reservations.model.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.facilityManagement.model.state.Room;

public class RoomData extends Room {
    private final String id;

    @JsonCreator
    public RoomData(
            @JsonProperty("id") String id,
            Room template
    ) {
        super(template.getName(), template.getMaintenance());
        this.id = id;
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }
}
