package de.vkb.dojo.es.humanResources.services.events.impl

import de.vkb.dojo.es.humanResources.model.aggregate.PersonAggregate
import de.vkb.dojo.es.humanResources.model.event.PersonCreated
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.model.feedback.SuccessFeedback
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult
import de.vkb.dojo.es.humanResources.services.events.delegating.PickyEventAggregator
import jakarta.inject.Singleton

@Singleton
class PersonDeletedAggregator: PickyEventAggregator<PersonEvent, PersonCreated, PersonAggregate>(PersonCreated::class.java) {
    override fun doProcess(
        event: PersonCreated,
        aggregate: PersonAggregate?
    ): EventAggregatorResult<PersonCreated, PersonAggregate> {
        if (aggregate == null) {
            return EventAggregatorResult(event, FailFeedback("person with this aggregateId not found"))
        }
        return EventAggregatorResult(
            event,
            null,
            SuccessFeedback(event.reference)
        )
    }
}
