package de.vkb.dojo.es.facilityManagement.services.events.impl;

import de.vkb.dojo.es.facilityManagement.model.aggregate.RoomAggregate;
import de.vkb.dojo.es.facilityManagement.model.event.RoomDeleted;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.feedback.FailFeedback;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;
import de.vkb.dojo.es.facilityManagement.services.events.delegating.PickyEventAggregator;
import org.springframework.stereotype.Component;

@Component
public class RoomDeletedEventAggregator extends PickyEventAggregator<RoomEvent, RoomDeleted, RoomAggregate> {
    public RoomDeletedEventAggregator() {
        super(RoomDeleted.class);
    }

    @Override
    protected EventAggregatorResult<RoomDeleted, RoomAggregate> doProcess(RoomDeleted event, RoomAggregate aggregate) {
        if (aggregate == null) {
            return new EventAggregatorResult<>(event, new FailFeedback("no room with this aggregateId found"));
        }
        return new EventAggregatorResult<>(
                event,
                null
        );
    }
}
