package de.vkb.dojo.es.humanResources.services.commands.impl

import jakarta.inject.Singleton
import de.vkb.dojo.es.humanResources.model.command.CreatePerson
import de.vkb.dojo.es.humanResources.model.command.DeletePerson
import de.vkb.dojo.es.humanResources.model.command.MarkPersonAsHealthy
import de.vkb.dojo.es.humanResources.model.command.PersonCommand
import de.vkb.dojo.es.humanResources.model.event.PersonCreated
import de.vkb.dojo.es.humanResources.model.event.PersonDeleted
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.event.PersonHealthy
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult
import de.vkb.dojo.es.humanResources.services.commands.delegating.PickyCommandHandler

@Singleton
class MarkPersonHealthyHandler: PickyCommandHandler<PersonCommand, MarkPersonAsHealthy, PersonEvent, PersonHealthy>(MarkPersonAsHealthy::class.java) {
    override fun doHandle(command: MarkPersonAsHealthy): CommandHandlerResult<MarkPersonAsHealthy, PersonHealthy> {
        return CommandHandlerResult(
            command,
            PersonHealthy(
                operationId = command.operationId,
                aggregateId = command.aggregateId
            )
        )
    }
}
