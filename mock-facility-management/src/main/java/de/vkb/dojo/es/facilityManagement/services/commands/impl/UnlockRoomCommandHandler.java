package de.vkb.dojo.es.facilityManagement.services.commands.impl;

import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
import de.vkb.dojo.es.facilityManagement.model.command.UnlockRoom;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.event.RoomUnlocked;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;
import de.vkb.dojo.es.facilityManagement.services.commands.delegating.PickyCommandHandler;
import org.springframework.stereotype.Component;

@Component
public class UnlockRoomCommandHandler extends PickyCommandHandler<RoomCommand, UnlockRoom, RoomEvent, RoomUnlocked> {

public UnlockRoomCommandHandler() {
    super(UnlockRoom.class);
}

@Override
    public CommandHandlerResult<UnlockRoom, RoomUnlocked> doHandle(UnlockRoom command) {
        return new CommandHandlerResult<>(
                command,
                new RoomUnlocked(
                        command.getOperationId(),
                        command.getAggregateId()
                )
        );
    }
}
