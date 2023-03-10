package de.vkb.dojo.es.facilityManagement.services.commands.delegating;

import de.vkb.dojo.es.facilityManagement.model.command.Command;
import de.vkb.dojo.es.facilityManagement.model.event.Event;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandler;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DelegatingCommandHandler<C extends Command, E extends Event> implements CommandHandler<C, E> {
    private final List<PickyCommandHandler<C, ?, E, ?>> handlers = new ArrayList<>();

    public DelegatingCommandHandler(Collection<PickyCommandHandler<C, ?, E, ?>> handlers) {
        this.handlers.addAll(handlers);
    }

    public CommandHandlerResult<C, E> handle(C command) {
        for (PickyCommandHandler<C, ?, E, ?> handler : handlers) {
            if (handler.canHandle(command)) {
                return handler.handle(command);
            }
        }
        throw new IllegalArgumentException("no handler responsible for command of class " + command.getClass().getName());
    }
}
