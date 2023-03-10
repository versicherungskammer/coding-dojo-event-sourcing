package de.vkb.dojo.es.facilityManagement.services.commands.impl;

import de.vkb.dojo.es.facilityManagement.model.command.CreateRoom;
import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
import de.vkb.dojo.es.facilityManagement.model.event.RoomCreated;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.feedback.FailFeedback;
import de.vkb.dojo.es.facilityManagement.services.ValidatorService;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;
import de.vkb.dojo.es.facilityManagement.services.commands.delegating.PickyCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateRoomCommandHandler extends PickyCommandHandler<RoomCommand, CreateRoom, RoomEvent, RoomCreated> {
    @Autowired
    ValidatorService validatorService;

    public CreateRoomCommandHandler() {
        super(CreateRoom.class);
    }

    @Override
    public CommandHandlerResult<CreateRoom, RoomCreated> doHandle(CreateRoom command) {
        if (!validatorService.isValidName(command.getName())) {
            return new CommandHandlerResult<>(command, new FailFeedback("invalid name"));
        }
        return new CommandHandlerResult<>(
                command,
                new RoomCreated(
                        command.getOperationId(),
                        command.getAggregateId(),
                        command.getName()
                )
        );
    }
}
