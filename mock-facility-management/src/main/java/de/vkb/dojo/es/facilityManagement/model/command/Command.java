package de.vkb.dojo.es.facilityManagement.model.command;

public interface Command {
    String getOperationId();
    String getAggregateId();
}
