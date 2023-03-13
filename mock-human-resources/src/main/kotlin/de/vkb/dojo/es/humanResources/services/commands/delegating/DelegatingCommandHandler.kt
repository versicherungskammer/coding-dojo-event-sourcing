package de.vkb.dojo.es.humanResources.services.commands.delegating

import de.vkb.dojo.es.humanResources.model.command.Command
import de.vkb.dojo.es.humanResources.model.event.Event
import de.vkb.dojo.es.humanResources.services.commands.CommandHandler
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult


class DelegatingCommandHandler<C : Command, E : Event>(
    private val handlers: List<PickyCommandHandler<C, *, E, *>>
): CommandHandler<C, E> {

    override fun handle(command: C): CommandHandlerResult<C, E> {
        for (handler in handlers) {
            if (handler.canHandle(command)) {
                return handler.handle(command)
            }
        }
        throw IllegalArgumentException("no aggregator responsible for command of class " + command.javaClass.name)
    }
}
