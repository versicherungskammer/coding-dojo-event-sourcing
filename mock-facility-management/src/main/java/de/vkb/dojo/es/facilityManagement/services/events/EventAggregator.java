package de.vkb.dojo.es.facilityManagement.services.events;


import de.vkb.dojo.es.facilityManagement.model.event.Event;

public interface EventAggregator<E extends Event, A> {
    EventAggregatorResult<E, A> process(E event, A aggregate);
}
