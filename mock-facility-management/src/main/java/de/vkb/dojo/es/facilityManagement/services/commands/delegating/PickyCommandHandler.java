package de.vkb.dojo.es.facilityManagement.services.commands.delegating;

import de.vkb.dojo.es.facilityManagement.model.command.Command;
import de.vkb.dojo.es.facilityManagement.model.event.Event;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandler;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;

public abstract class PickyCommandHandler<PC extends Command, C extends PC, PE extends Event, E extends PE> implements CommandHandler<PC, PE> {
    private final Class<C> clazz;

    public PickyCommandHandler(Class<C> clazz) {
        this.clazz = clazz;
    }

    public Boolean canHandle(PC command) {
        return clazz.isAssignableFrom(command.getClass());
    }

    public CommandHandlerResult<PC, PE> handle(PC command) {
        if (!canHandle(command)) {
            throw new IllegalArgumentException("cannot handle command of class " + command.getClass().getName());
        }
        //noinspection unchecked
        return (CommandHandlerResult<PC, PE>) doHandle((C) command);
    }

    protected abstract CommandHandlerResult<C, E> doHandle(C command);
}
