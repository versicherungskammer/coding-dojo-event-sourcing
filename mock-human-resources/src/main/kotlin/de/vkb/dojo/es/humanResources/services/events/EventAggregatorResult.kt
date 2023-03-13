package de.vkb.dojo.es.humanResources.services.events

import de.vkb.dojo.es.humanResources.model.event.Event
import de.vkb.dojo.es.humanResources.model.feedback.Feedback
import de.vkb.dojo.es.humanResources.model.feedback.SuccessFeedback

data class EventAggregatorResult<E: Event, A>(
    val event: E,
    val aggregate: A?,
    val feedback: Feedback
)

fun <E: Event, A> EventAggregatorResult(event: E, feedback: Feedback): EventAggregatorResult<E, A> = EventAggregatorResult(event, null, feedback)
fun <E: Event, A> EventAggregatorResult(event: E, aggregate: A): EventAggregatorResult<E, A> = EventAggregatorResult(event, aggregate, SuccessFeedback(event.reference))
