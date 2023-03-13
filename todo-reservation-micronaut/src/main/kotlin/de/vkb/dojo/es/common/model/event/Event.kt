package de.vkb.dojo.es.common.model.event

interface Event {
    val operationId: String
    val aggregateId: String
}
