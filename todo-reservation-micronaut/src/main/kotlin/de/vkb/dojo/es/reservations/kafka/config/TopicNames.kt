package de.vkb.dojo.es.reservations.kafka.config

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("kafka.topics")
interface TopicNames {
    val roomEvent: String
    val roomEventAndState: String
    val roomState: String
    val personEvent: String
    val personEventAndState: String
    val personState: String
    val reservationCommand: String
    val reservationEventInternal: String
    val reservationEventExternal: String
    val reservationEventAndState: String
    val reservationState: String
    val feedback: String
}
