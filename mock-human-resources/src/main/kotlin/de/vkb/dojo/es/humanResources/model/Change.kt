package de.vkb.dojo.es.humanResources.model

data class Change<T>(
    val from: T,
    val to: T
)
