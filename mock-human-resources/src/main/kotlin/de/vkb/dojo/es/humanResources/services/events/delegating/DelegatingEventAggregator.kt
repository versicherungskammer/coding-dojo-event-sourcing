package de.vkb.dojo.es.humanResources.services.events.delegating

import de.vkb.dojo.es.humanResources.model.event.Event
import de.vkb.dojo.es.humanResources.services.events.EventAggregator
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult


class DelegatingEventAggregator<E : Event, A>(aggregators: Collection<PickyEventAggregator<E, *, A>>?) :
    EventAggregator<E, A> {
    private val aggregators: MutableList<PickyEventAggregator<E, *, A>> = ArrayList()

    init {
        this.aggregators.addAll(aggregators!!)
    }

    override fun process(event: E, aggregate: A?): EventAggregatorResult<E, A> {
        for (aggregator in aggregators) {
            if (aggregator.canProcess(event)) {
                return aggregator.process(event, aggregate)
            }
        }
        throw IllegalArgumentException("no aggregator responsible for event of class " + event.javaClass.name)
    }
}
