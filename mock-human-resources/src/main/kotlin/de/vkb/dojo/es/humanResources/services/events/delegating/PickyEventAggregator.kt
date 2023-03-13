package de.vkb.dojo.es.humanResources.services.events.delegating

import de.vkb.dojo.es.humanResources.model.event.Event
import de.vkb.dojo.es.humanResources.services.events.EventAggregator
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult

abstract class PickyEventAggregator<PE : Event, E : PE, A>(private val clazz: Class<E>) : EventAggregator<PE, A> {
    fun canProcess(event: PE): Boolean {
        return clazz.isAssignableFrom(event.javaClass)
    }

    override fun process(event: PE, aggregate: A?): EventAggregatorResult<PE, A> {
        require(canProcess(event)) { "cannot handle event of class " + event.javaClass.name }
        @Suppress("UNCHECKED_CAST")
        return doProcess(event as E, aggregate) as EventAggregatorResult<PE, A>
    }

    protected abstract fun doProcess(event: E, aggregate: A?): EventAggregatorResult<E, A>
}
