package de.vkb.dojo.es.humanResources.services.commands.impl

import jakarta.inject.Singleton
import de.vkb.dojo.es.humanResources.model.command.CreatePerson
import de.vkb.dojo.es.humanResources.model.command.PersonCommand
import de.vkb.dojo.es.humanResources.model.event.PersonCreated
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult
import de.vkb.dojo.es.humanResources.services.commands.delegating.PickyCommandHandler

@Singleton
class CreatePersonHandler(
    private val validatorService: ValidatorService
): PickyCommandHandler<PersonCommand, CreatePerson, PersonEvent, PersonCreated>(CreatePerson::class.java) {
    override fun doHandle(command: CreatePerson): CommandHandlerResult<CreatePerson, PersonCreated> {
        if (!validatorService.isValidUsername(command.username)) {
            return CommandHandlerResult(command, FailFeedback("invalid username"))
        }
        if (!validatorService.isValidFullname(command.fullname)) {
            return CommandHandlerResult(command, FailFeedback("invalid fullname"))
        }
        return CommandHandlerResult(
            command,
            PersonCreated(
                operationId = command.operationId,
                aggregateId = command.aggregateId,
                username = command.username,
                fullname = command.fullname,
            )
        )
    }
}
