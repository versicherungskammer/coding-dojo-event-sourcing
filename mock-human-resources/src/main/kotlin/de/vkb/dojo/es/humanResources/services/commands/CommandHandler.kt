package de.vkb.dojo.es.humanResources.services.commands

import de.vkb.dojo.es.humanResources.model.command.Command
import de.vkb.dojo.es.humanResources.model.event.Event

interface CommandHandler<C: Command, E: Event> {
    fun handle(command: C): CommandHandlerResult<C, E>
}
