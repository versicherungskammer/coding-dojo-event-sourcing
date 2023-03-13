package de.vkb.dojo.es.humanResources.kafka

import de.vkb.dojo.es.humanResources.model.command.PersonCommand
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient(
//    id = "SYS-KAF-TIER-T-PAZ01",
    id = "\${kafka.producers.person-command.id}",
    acks = KafkaClient.Acknowledge.ALL
)
interface PersonCommandProducer {
    @Topic("\${kafka.topics.personCommand}")
    fun sendCommand(@KafkaKey key: String, command: PersonCommand)
}
