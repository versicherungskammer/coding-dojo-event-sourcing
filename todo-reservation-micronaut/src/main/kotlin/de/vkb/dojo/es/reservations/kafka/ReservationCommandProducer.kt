package de.vkb.dojo.es.reservations.kafka

import de.vkb.dojo.es.reservations.model.command.ReservationCommand
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient(
    id = "\${kafka.producers.reservation-command.id}",
    acks = KafkaClient.Acknowledge.ALL
)
interface ReservationCommandProducer {
    @Topic("\${kafka.topics.reservationCommand}")
    fun sendCommand(@KafkaKey key: String, command: ReservationCommand)
}
