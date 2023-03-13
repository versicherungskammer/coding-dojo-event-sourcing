package de.vkb.dojo.es.reservations.model.command

interface Command {
    val operationId: String
    val aggregateId: String
}
