package de.vkb.dojo.es.facilityManagement.model.dto;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.facilityManagement.model.ref.RoomReference;
import de.vkb.dojo.es.facilityManagement.model.state.Room;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomOutput extends Room {
    private final String id;
    private final RoomReference ref;
    private final Map<String,String> links = new HashMap<>();

    @JsonCreator
    public RoomOutput(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("maintenance") Boolean maintenance
    ) {
        super(name, maintenance);
        this.id = id;
        this.ref = new RoomReference(id);
        this.links.put("self", ref.getPath());
    }

    @JsonCreator
    public RoomOutput(
            @JsonProperty("id") String id,
            Room template
    ) {
        super(template);
        this.id = id;
        this.ref = new RoomReference(id);
        this.links.put("self", ref.getPath());
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }

    @JsonGetter("_links")
    public Map<String,String> getLinks() {
        return links;
    }

    @JsonIgnore
    public RoomReference getRef() {
        return ref;
    }
}
