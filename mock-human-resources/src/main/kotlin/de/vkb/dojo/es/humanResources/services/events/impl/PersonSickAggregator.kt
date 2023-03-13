package de.vkb.dojo.es.humanResources.services.events.impl

import de.vkb.dojo.es.humanResources.model.aggregate.PersonAggregate
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.event.PersonSick
import de.vkb.dojo.es.humanResources.model.event.PersonUpdated
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult
import de.vkb.dojo.es.humanResources.services.events.delegating.PickyEventAggregator
import jakarta.inject.Singleton

@Singleton
class PersonSickAggregator: PickyEventAggregator<PersonEvent, PersonSick, PersonAggregate>(PersonSick::class.java) {
    override fun doProcess(
        event: PersonSick,
        aggregate: PersonAggregate?
    ): EventAggregatorResult<PersonSick, PersonAggregate> {
        if (aggregate == null) {
            return EventAggregatorResult(event, FailFeedback("person with this aggregateId not found"))
        }
        return EventAggregatorResult(
            event,
            aggregate.copy(
                person = aggregate.person.copy(
                    sick = true
                )
            )
        )
    }
}
