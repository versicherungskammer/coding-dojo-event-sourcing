package de.vkb.dojo.es.reservations.kafka.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

@Singleton
data class StoreNames(
    @Property(name = "kafka.streams.reservation-command-handler.stores.rooms") val reservationCommandRooms: String,
    @Property(name = "kafka.streams.reservation-command-handler.stores.persons") val reservationCommandPersons: String,
    @Property(name = "kafka.streams.default.stores.aggregate") val reservationEventAggregate: String,
    @Property(name = "kafka.streams.reservation-room-saga.stores.rooms") val roomSagaStore: String,
    @Property(name = "kafka.streams.reservation-person-saga.stores.persons") val personSagaStore: String,
    @Property(name = "kafka.streams.reservation-reader.stores.state") val reservationReaderState: String,
    @Property(name = "kafka.streams.reservation-feedback-reader.stores.state") val feedbackReaderState: String
)
