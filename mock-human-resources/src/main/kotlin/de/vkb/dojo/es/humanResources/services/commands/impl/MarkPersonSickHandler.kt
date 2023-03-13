package de.vkb.dojo.es.humanResources.services.commands.impl

import jakarta.inject.Singleton
import de.vkb.dojo.es.humanResources.model.command.CreatePerson
import de.vkb.dojo.es.humanResources.model.command.DeletePerson
import de.vkb.dojo.es.humanResources.model.command.MarkPersonAsSick
import de.vkb.dojo.es.humanResources.model.command.PersonCommand
import de.vkb.dojo.es.humanResources.model.event.PersonCreated
import de.vkb.dojo.es.humanResources.model.event.PersonDeleted
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.event.PersonSick
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult
import de.vkb.dojo.es.humanResources.services.commands.delegating.PickyCommandHandler

@Singleton
class MarkPersonSickHandler: PickyCommandHandler<PersonCommand, MarkPersonAsSick, PersonEvent, PersonSick>(MarkPersonAsSick::class.java) {
    override fun doHandle(command: MarkPersonAsSick): CommandHandlerResult<MarkPersonAsSick, PersonSick> {
        return CommandHandlerResult(
            command,
            PersonSick(
                operationId = command.operationId,
                aggregateId = command.aggregateId
            )
        )
    }
}
