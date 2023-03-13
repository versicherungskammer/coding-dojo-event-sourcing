package de.vkb.dojo.es.humanResources.services.commands.impl

import jakarta.inject.Singleton
import de.vkb.dojo.es.humanResources.model.command.CreatePerson
import de.vkb.dojo.es.humanResources.model.command.DeletePerson
import de.vkb.dojo.es.humanResources.model.command.PersonCommand
import de.vkb.dojo.es.humanResources.model.event.PersonCreated
import de.vkb.dojo.es.humanResources.model.event.PersonDeleted
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult
import de.vkb.dojo.es.humanResources.services.commands.delegating.PickyCommandHandler

@Singleton
class DeletePersonHandler: PickyCommandHandler<PersonCommand, DeletePerson, PersonEvent, PersonDeleted>(DeletePerson::class.java) {
    override fun doHandle(command: DeletePerson): CommandHandlerResult<DeletePerson, PersonDeleted> {
        return CommandHandlerResult(
            command,
            PersonDeleted(
                operationId = command.operationId,
                aggregateId = command.aggregateId
            )
        )
    }
}
