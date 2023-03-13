package de.vkb.dojo.es.humanResources.services.commands.delegating

import de.vkb.dojo.es.humanResources.model.command.Command
import de.vkb.dojo.es.humanResources.model.event.Event
import de.vkb.dojo.es.humanResources.services.commands.CommandHandler
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult

abstract class PickyCommandHandler<PC : Command, C : PC, PE : Event, E : PE>(private val clazz: Class<C>) : CommandHandler<PC, PE> {
    fun canHandle(command: PC): Boolean {
        return clazz.isAssignableFrom(command.javaClass)
    }

    override fun handle(command: PC): CommandHandlerResult<PC, PE> {
        require(canHandle(command)) { "cannot handle command of class " + command.javaClass.name }
        @Suppress("UNCHECKED_CAST")
        return doHandle(command as C) as CommandHandlerResult<PC, PE>
    }

    protected abstract fun doHandle(command: C): CommandHandlerResult<C, E>
}
