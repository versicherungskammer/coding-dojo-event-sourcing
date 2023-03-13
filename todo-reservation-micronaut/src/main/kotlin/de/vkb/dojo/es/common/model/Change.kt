package de.vkb.dojo.es.common.model

data class Change<T>(
    val from: T,
    val to: T
)
