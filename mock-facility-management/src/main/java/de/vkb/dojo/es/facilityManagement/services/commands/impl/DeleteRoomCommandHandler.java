package de.vkb.dojo.es.facilityManagement.services.commands.impl;

import de.vkb.dojo.es.facilityManagement.model.command.DeleteRoom;
import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
import de.vkb.dojo.es.facilityManagement.model.event.RoomDeleted;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;
import de.vkb.dojo.es.facilityManagement.services.commands.delegating.PickyCommandHandler;
import org.springframework.stereotype.Component;

@Component
public class DeleteRoomCommandHandler extends PickyCommandHandler<RoomCommand, DeleteRoom, RoomEvent, RoomDeleted> {
    public DeleteRoomCommandHandler() {
        super(DeleteRoom.class);
    }

    @Override
    public CommandHandlerResult<DeleteRoom, RoomDeleted> doHandle(DeleteRoom command) {
        return new CommandHandlerResult<>(
                command,
                new RoomDeleted(
                        command.getOperationId(),
                        command.getAggregateId()
                )
        );
    }
}
