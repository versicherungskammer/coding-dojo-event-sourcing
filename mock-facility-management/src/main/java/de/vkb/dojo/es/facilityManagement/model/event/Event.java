package de.vkb.dojo.es.facilityManagement.model.event;

import de.vkb.dojo.es.facilityManagement.model.feedback.ref.Reference;

public interface Event {
    String getOperationId();
    String getAggregateId();
    Reference getReference();
}
