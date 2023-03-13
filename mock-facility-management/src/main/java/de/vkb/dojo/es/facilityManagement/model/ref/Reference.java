package de.vkb.dojo.es.facilityManagement.model.ref;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.vkb.dojo.es.facilityManagement.model.feedback.Feedback;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomReference.class, name = "room"),
        @JsonSubTypes.Type(value = FeedbackReference.class, name = "feedback")
})
public interface Reference {
    String getId();
    String getPath();
}
