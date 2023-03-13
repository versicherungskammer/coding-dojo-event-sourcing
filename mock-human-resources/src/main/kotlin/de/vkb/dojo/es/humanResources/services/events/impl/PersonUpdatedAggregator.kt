package de.vkb.dojo.es.humanResources.services.events.impl

import de.vkb.dojo.es.humanResources.model.aggregate.PersonAggregate
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.event.PersonUpdated
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult
import de.vkb.dojo.es.humanResources.services.events.delegating.PickyEventAggregator
import jakarta.inject.Singleton

@Singleton
class PersonUpdatedAggregator(
    private val validatorService: ValidatorService
): PickyEventAggregator<PersonEvent, PersonUpdated, PersonAggregate>(PersonUpdated::class.java) {
    override fun doProcess(
        event: PersonUpdated,
        aggregate: PersonAggregate?
    ): EventAggregatorResult<PersonUpdated, PersonAggregate> {
        if (aggregate == null) {
            return EventAggregatorResult(event, FailFeedback("person with this aggregateId not found"))
        }
        var person = aggregate.person
        if (event.username != null) {
            if (!validatorService.isValidUsername(event.username.to)) {
                return EventAggregatorResult(event, FailFeedback("invalid username"))
            }
            if (person.username != event.username.from) {
                return EventAggregatorResult(event, FailFeedback("conflict detected"))
            }
            person = person.copy(
                username = event.username.to
            )
        }
        if (event.fullname != null) {
            if (!validatorService.isValidFullname(event.fullname.to)) {
                return EventAggregatorResult(event, FailFeedback("invalid fullName"))
            }
            if (person.fullname != event.fullname.from) {
                return EventAggregatorResult(event, FailFeedback("conflict detected"))
            }
            person = person.copy(
                fullname = event.fullname.to
            )
        }
        return EventAggregatorResult(
            event,
            aggregate.copy(
                person = person
            )
        )
    }
}
