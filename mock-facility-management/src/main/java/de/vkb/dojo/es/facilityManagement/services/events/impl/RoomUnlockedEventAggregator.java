package de.vkb.dojo.es.facilityManagement.services.events.impl;

import de.vkb.dojo.es.facilityManagement.model.aggregate.RoomAggregate;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.event.RoomUnlocked;
import de.vkb.dojo.es.facilityManagement.model.feedback.FailFeedback;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;
import de.vkb.dojo.es.facilityManagement.services.events.delegating.PickyEventAggregator;
import org.springframework.stereotype.Component;

@Component
public class RoomUnlockedEventAggregator extends PickyEventAggregator<RoomEvent, RoomUnlocked, RoomAggregate> {
    public RoomUnlockedEventAggregator() {
        super(RoomUnlocked.class);
    }

    @Override
    protected EventAggregatorResult<RoomUnlocked, RoomAggregate> doProcess(RoomUnlocked event, RoomAggregate aggregate) {
        if (aggregate == null) {
            return new EventAggregatorResult<>(event, new FailFeedback("no room with this aggregateId found"));
        }
        if (!aggregate.getRoom().getMaintenance()) {
            return new EventAggregatorResult<>(event, new FailFeedback("room is not locked"));
        }
        return new EventAggregatorResult<>(
                event,
                aggregate.modify( builder ->
                        builder.maintenance(false)
                )
        );
    }
}
