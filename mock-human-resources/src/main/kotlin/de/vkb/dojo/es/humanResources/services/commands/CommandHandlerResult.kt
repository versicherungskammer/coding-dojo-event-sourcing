package de.vkb.dojo.es.humanResources.services.commands

import de.vkb.dojo.es.humanResources.model.command.Command
import de.vkb.dojo.es.humanResources.model.event.Event
import de.vkb.dojo.es.humanResources.model.feedback.Feedback
import de.vkb.dojo.es.humanResources.model.feedback.SuccessFeedback

data class CommandHandlerResult<C: Command, E: Event>(
    val command: C,
    val event: E?,
    val feedback: Feedback
)

fun <C: Command, E: Event> CommandHandlerResult(command: C, feedback: Feedback): CommandHandlerResult<C, E> = CommandHandlerResult(command, null, feedback)
fun <C: Command, E: Event> CommandHandlerResult(command: C, event: E): CommandHandlerResult<C, E> = CommandHandlerResult(command, event, SuccessFeedback(event.reference))
