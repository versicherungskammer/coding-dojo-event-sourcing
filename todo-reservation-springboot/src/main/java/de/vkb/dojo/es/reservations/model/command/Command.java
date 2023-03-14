package de.vkb.dojo.es.reservations.model.command;

public interface Command {
    String getOperationId();
    String getAggregateId();
}
