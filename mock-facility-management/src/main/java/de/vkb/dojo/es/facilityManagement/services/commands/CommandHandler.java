package de.vkb.dojo.es.facilityManagement.services.commands;


import de.vkb.dojo.es.facilityManagement.model.command.Command;
import de.vkb.dojo.es.facilityManagement.model.event.Event;

public interface CommandHandler<C extends Command, E extends Event> {
    CommandHandlerResult<C,E> handle(C command);
}
