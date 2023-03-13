package de.vkb.dojo.es.humanResources.services.events

import de.vkb.dojo.es.humanResources.model.event.Event

interface EventAggregator<E: Event, A> {
    fun process(event: E, aggregate: A?): EventAggregatorResult<E, A>
}
