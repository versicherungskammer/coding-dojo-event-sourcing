package de.vkb.dojo.es.facilityManagement.services.events.impl;

import de.vkb.dojo.es.facilityManagement.model.Change;
import de.vkb.dojo.es.facilityManagement.model.aggregate.RoomAggregate;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.event.RoomUpdated;
import de.vkb.dojo.es.facilityManagement.model.feedback.FailFeedback;
import de.vkb.dojo.es.facilityManagement.services.ValidatorService;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;
import de.vkb.dojo.es.facilityManagement.services.events.delegating.PickyEventAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoomUpdatedEventAggregator extends PickyEventAggregator<RoomEvent, RoomUpdated, RoomAggregate> {
    @Autowired
    ValidatorService validatorService;
    
    public RoomUpdatedEventAggregator() {
        super(RoomUpdated.class);
    }

    @Override
    protected EventAggregatorResult<RoomUpdated, RoomAggregate> doProcess(RoomUpdated event, RoomAggregate aggregate) {
        if (!validatorService.isValidName(Optional.ofNullable(event.getName()).map(Change::getTo))) {
            return new EventAggregatorResult<>(event, new FailFeedback("invalid name"));
        }
        if (event.getName().getTo().equals(event.getName().getFrom())) {
            return new EventAggregatorResult<>(event, new FailFeedback("nothing changed"));
        }
        if (aggregate == null) {
            return new EventAggregatorResult<>(event, new FailFeedback("no room with this aggregateId found"));
        }
        if (!aggregate.getRoom().getName().equals(event.getName().getFrom())) {
            return new EventAggregatorResult<>(event, new FailFeedback("conflict detected"));
        }
        return new EventAggregatorResult<>(
                event,
                aggregate.modify( builder ->
                        builder.name(event.getName().getTo())
                )
        );
    }
}
