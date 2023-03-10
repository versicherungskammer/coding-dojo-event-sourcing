package de.vkb.dojo.es.facilityManagement.model.feedback.ref;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomReference.class, name = "room")
})
public interface Reference {
    String getId();
    String getPath();
}
