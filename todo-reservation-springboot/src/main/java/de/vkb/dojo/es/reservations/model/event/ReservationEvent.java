package de.vkb.dojo.es.reservations.model.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.vkb.dojo.es.common.model.event.Event;
import de.vkb.dojo.es.common.model.ref.Reference;
import de.vkb.dojo.es.reservations.model.ref.ReservationReference;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReservationCreated.class, name = "reservatipn-created"),
        @JsonSubTypes.Type(value = ReservationDeleted.class, name = "reservatipn-deleted")
})
public abstract class ReservationEvent implements Event {
    public abstract String getOperationId();
    public abstract String getAggregateId();
    public Reference getReference() {
        return new ReservationReference(getAggregateId());
    }
}
