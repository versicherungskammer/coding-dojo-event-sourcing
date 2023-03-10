package de.vkb.dojo.es.facilityManagement.services.commands.impl;

import de.vkb.dojo.es.facilityManagement.model.Change;
import de.vkb.dojo.es.facilityManagement.model.command.EditRoom;
import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.event.RoomUpdated;
import de.vkb.dojo.es.facilityManagement.model.feedback.FailFeedback;
import de.vkb.dojo.es.facilityManagement.services.ValidatorService;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;
import de.vkb.dojo.es.facilityManagement.services.commands.delegating.PickyCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EditRoomCommandHandler extends PickyCommandHandler<RoomCommand, EditRoom, RoomEvent, RoomUpdated> {
    @Autowired
    ValidatorService validatorService;

    public EditRoomCommandHandler() {
        super(EditRoom.class);
    }

    @Override
    public CommandHandlerResult<EditRoom, RoomUpdated> doHandle(EditRoom command) {
        if (!validatorService.isValidName(Optional.ofNullable(command.getName()).map(Change::getTo))) {
            return new CommandHandlerResult<>(command, new FailFeedback("invalid name"));
        }
        if (command.getName().getTo().equals(command.getName().getFrom())) {
            return new CommandHandlerResult<>(command, new FailFeedback("nothing changed"));
        }
        return new CommandHandlerResult<>(
                command,
                new RoomUpdated(
                        command.getOperationId(),
                        command.getAggregateId(),
                        command.getName()
                )
        );
    }
}
