package de.vkb.dojo.es.facilityManagement.services.events.delegating;

import de.vkb.dojo.es.facilityManagement.model.event.Event;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregator;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;

public abstract class PickyEventAggregator<PE extends Event, E extends PE, A> implements EventAggregator<PE, A> {
    private final Class<E> clazz;

    public PickyEventAggregator(Class<E> clazz) {
        this.clazz = clazz;
    }

    public Boolean canProcess(PE event) {
        return clazz.isAssignableFrom(event.getClass());
    }

    @Override
    public EventAggregatorResult<PE, A> process(PE event, A aggregate) {
        if (!canProcess(event)) {
            throw new IllegalArgumentException("cannot handle event of class " + event.getClass().getName());
        }
        //noinspection unchecked
        return (EventAggregatorResult<PE, A>) doProcess((E) event, aggregate);
    }

    protected abstract EventAggregatorResult<E, A> doProcess(E event, A aggregate);
}
