package de.vkb.dojo.es.humanResources.kafka.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

@Singleton
data class StoreNames(
    @Property(name = "kafka.streams.default.stores.aggregate") val personEventAggregate: String,
    @Property(name = "kafka.streams.person-reader.stores.state") val personReaderState: String,
    @Property(name = "kafka.streams.feedback-reader.stores.state") val feedbackReaderState: String
)
