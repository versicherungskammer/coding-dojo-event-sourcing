package de.vkb.dojo.es.facilityManagement.services.commands;


import de.vkb.dojo.es.facilityManagement.model.command.Command;
import de.vkb.dojo.es.facilityManagement.model.event.Event;
import de.vkb.dojo.es.facilityManagement.model.feedback.Feedback;
import de.vkb.dojo.es.facilityManagement.model.feedback.SuccessFeedback;

public class CommandHandlerResult<C extends Command, E extends Event> {
    private final C command;
    private final E event;
    private final Feedback feedback;

    public CommandHandlerResult(C command, E event) {
        this.command = command;
        this.event = event;
        this.feedback = new SuccessFeedback(event.getReference());
    }

    public CommandHandlerResult(C command, Feedback feedback) {
        this.command = command;
        this.event = null;
        this.feedback = feedback;
    }

    public C getCommand() {
        return command;
    }

    public E getEvent() {
        return event;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}
