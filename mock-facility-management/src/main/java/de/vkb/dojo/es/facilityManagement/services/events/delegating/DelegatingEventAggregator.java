package de.vkb.dojo.es.facilityManagement.services.events.delegating;

import de.vkb.dojo.es.facilityManagement.model.event.Event;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregator;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DelegatingEventAggregator<E extends Event, A> implements EventAggregator<E, A> {
    private final List<PickyEventAggregator<E, ?, A>> aggregators = new ArrayList<>();

    public DelegatingEventAggregator(Collection<PickyEventAggregator<E, ?, A>> aggregators) {
        this.aggregators.addAll(aggregators);
    }

    public EventAggregatorResult<E, A> process(E event, A aggregate) {
        for (PickyEventAggregator<E, ?, A> aggregator : aggregators) {
            if (aggregator.canProcess(event)) {
                return aggregator.process(event, aggregate);
            }
        }
        throw new IllegalArgumentException("no aggregator responsible for event of class " + event.getClass().getName());
    }
}
