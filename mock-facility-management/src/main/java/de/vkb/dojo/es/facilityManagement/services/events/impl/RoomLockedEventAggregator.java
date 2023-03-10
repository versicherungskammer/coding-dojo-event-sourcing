package de.vkb.dojo.es.facilityManagement.services.events.impl;

import de.vkb.dojo.es.facilityManagement.model.aggregate.RoomAggregate;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.event.RoomLocked;
import de.vkb.dojo.es.facilityManagement.model.feedback.FailFeedback;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;
import de.vkb.dojo.es.facilityManagement.services.events.delegating.PickyEventAggregator;
import org.springframework.stereotype.Component;

@Component
public class RoomLockedEventAggregator extends PickyEventAggregator<RoomEvent, RoomLocked, RoomAggregate> {
        public RoomLockedEventAggregator() {
        super(RoomLocked.class);
    }

    @Override
    protected EventAggregatorResult<RoomLocked, RoomAggregate> doProcess(RoomLocked event, RoomAggregate aggregate) {
        if (aggregate == null) {
            return new EventAggregatorResult<>(event, new FailFeedback("no room with this aggregateId found"));
        }
        if (aggregate.getRoom().getMaintenance()) {
            return new EventAggregatorResult<>(event, new FailFeedback("room is already locked"));
        }
        return new EventAggregatorResult<>(
                event,
                aggregate.modify( builder ->
                        builder.maintenance(true)
                )
        );
    }
}
