package de.vkb.dojo.es.humanResources.services.events.impl

import de.vkb.dojo.es.humanResources.model.aggregate.PersonAggregate
import de.vkb.dojo.es.humanResources.model.event.PersonCreated
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.model.state.Person
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult
import de.vkb.dojo.es.humanResources.services.events.delegating.PickyEventAggregator
import jakarta.inject.Singleton

@Singleton
class PersonCreatedAggregator(
    private val validatorService: ValidatorService
): PickyEventAggregator<PersonEvent, PersonCreated, PersonAggregate>(PersonCreated::class.java) {
    override fun doProcess(
        event: PersonCreated,
        aggregate: PersonAggregate?
    ): EventAggregatorResult<PersonCreated, PersonAggregate> {
        if (!validatorService.isValidUsername(event.username)) {
            return EventAggregatorResult(event, FailFeedback("invalid username"))
        }
        if (!validatorService.isValidFullname(event.fullname)) {
            return EventAggregatorResult(event, FailFeedback("invalid fullname"))
        }
        if (aggregate != null) {
            return EventAggregatorResult(event, FailFeedback("person with this aggregateId already present"))
        }
        return EventAggregatorResult(
            event,
            PersonAggregate(
                id = event.aggregateId,
                person = Person(
                    username = event.username,
                    fullname = event.fullname
                )
            )
        )
    }
}
