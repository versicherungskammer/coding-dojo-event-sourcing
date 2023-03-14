package de.vkb.dojo.es.facilityManagement.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {
    private final String name;
    private final Boolean maintenance;

    @JsonCreator
    public Room(
            @JsonProperty("name") String name,
            @JsonProperty("maintenance") Boolean maintenance
    ) {
        this.name = name;
        this.maintenance = maintenance;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonGetter
    public Boolean getMaintenance() {
        return maintenance;
    }
}
