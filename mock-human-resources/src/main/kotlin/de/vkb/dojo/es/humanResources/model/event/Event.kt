package de.vkb.dojo.es.humanResources.model.event

import de.vkb.dojo.es.humanResources.model.ref.Reference

interface Event {
    val operationId: String
    val aggregateId: String
    val reference: Reference
}
