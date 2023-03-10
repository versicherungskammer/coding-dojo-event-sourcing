package de.vkb.dojo.es.facilityManagement.services.commands.impl;

import de.vkb.dojo.es.facilityManagement.model.command.LockRoom;
import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.event.RoomLocked;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;
import de.vkb.dojo.es.facilityManagement.services.commands.delegating.PickyCommandHandler;
import org.springframework.stereotype.Component;

@Component
public class LockRoomCommandHandler extends PickyCommandHandler<RoomCommand, LockRoom, RoomEvent, RoomLocked> {

    public LockRoomCommandHandler() {
        super(LockRoom.class);
    }

    @Override
    public CommandHandlerResult<LockRoom, RoomLocked> doHandle(LockRoom command) {
        return new CommandHandlerResult<>(
                command,
                new RoomLocked(
                        command.getOperationId(),
                        command.getAggregateId()
                )
        );
    }
}
