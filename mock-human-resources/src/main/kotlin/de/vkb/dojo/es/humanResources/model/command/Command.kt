package de.vkb.dojo.es.humanResources.model.command

interface Command {
    val operationId: String
    val aggregateId: String
}
