package de.vkb.dojo.es.humanResources.kafka.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

@ConfigurationProperties("kafka.topics")
interface TopicNames {
    val personCommand: String
    val personEventInternal: String
    val personEventExternal: String
    val personEventAndState: String
    val personState: String
    val feedback: String
}
