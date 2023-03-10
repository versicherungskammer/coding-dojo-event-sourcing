package de.vkb.dojo.es.facilityManagement.model.state.builder;

import de.vkb.dojo.es.facilityManagement.model.state.Room;

public class RoomBuilder {
    private String name;
    private Boolean maintenance;

    public RoomBuilder() {
    }
    public RoomBuilder(Room template) {
        this.name = template.getName();
        this.maintenance = template.getMaintenance();
    }

    public RoomBuilder name(String v) {
        this.name = v;
        return this;
    }

    public RoomBuilder maintenance(Boolean v) {
        this.maintenance = v;
        return this;
    }

    public Room build() {
        return new Room(name, maintenance);
    }
}
