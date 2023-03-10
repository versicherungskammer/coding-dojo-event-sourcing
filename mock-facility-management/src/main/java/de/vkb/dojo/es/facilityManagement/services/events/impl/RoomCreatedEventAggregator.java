package de.vkb.dojo.es.facilityManagement.services.events.impl;

import de.vkb.dojo.es.facilityManagement.model.aggregate.RoomAggregate;
import de.vkb.dojo.es.facilityManagement.model.event.RoomCreated;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.feedback.FailFeedback;
import de.vkb.dojo.es.facilityManagement.model.state.Room;
import de.vkb.dojo.es.facilityManagement.services.ValidatorService;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;
import de.vkb.dojo.es.facilityManagement.services.events.delegating.PickyEventAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomCreatedEventAggregator extends PickyEventAggregator<RoomEvent, RoomCreated, RoomAggregate> {
    @Autowired
    ValidatorService validatorService;

    public RoomCreatedEventAggregator() {
        super(RoomCreated.class);
    }

    @Override
    protected EventAggregatorResult<RoomCreated, RoomAggregate> doProcess(RoomCreated event, RoomAggregate aggregate) {
        if (!validatorService.isValidName(event.getName())) {
            return new EventAggregatorResult<>(event, new FailFeedback("invalid name"));
        }
        if (aggregate != null) {
            return new EventAggregatorResult<>(event, new FailFeedback("room with this aggregateId already present"));
        }
        return new EventAggregatorResult<>(
                event,
                new RoomAggregate(
                        event.getAggregateId(),
                        new Room(event.getName(), true)
                )
        );
    }
}
