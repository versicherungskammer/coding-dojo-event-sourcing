package de.vkb.dojo.es.humanResources.services.commands.impl

import jakarta.inject.Singleton
import de.vkb.dojo.es.humanResources.model.command.CreatePerson
import de.vkb.dojo.es.humanResources.model.command.EditPerson
import de.vkb.dojo.es.humanResources.model.command.PersonCommand
import de.vkb.dojo.es.humanResources.model.event.PersonCreated
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.event.PersonUpdated
import de.vkb.dojo.es.humanResources.model.feedback.FailFeedback
import de.vkb.dojo.es.humanResources.services.ValidatorService
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult
import de.vkb.dojo.es.humanResources.services.commands.delegating.PickyCommandHandler

@Singleton
class EditPersonHandler(
    private val validatorService: ValidatorService
): PickyCommandHandler<PersonCommand, EditPerson, PersonEvent, PersonUpdated>(EditPerson::class.java) {
    override fun doHandle(command: EditPerson): CommandHandlerResult<EditPerson, PersonUpdated> {
        if (command.username != null && !validatorService.isValidUsername(command.username.to)) {
            return CommandHandlerResult(command, FailFeedback("invalid username"))
        }
        if (command.fullname != null && !validatorService.isValidFullname(command.fullname.to)) {
            return CommandHandlerResult(command, FailFeedback("invalid fullname"))
        }
        return CommandHandlerResult(
            command,
            PersonUpdated(
                operationId = command.operationId,
                aggregateId = command.aggregateId,
                username = command.username,
                fullname = command.fullname,
            )
        )
    }
}
