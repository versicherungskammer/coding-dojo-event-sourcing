package de.vkb.dojo.es.reservations.kafka

import de.vkb.dojo.es.common.model.feedback.Feedback
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.reservations.kafka.config.StoreNames
import de.vkb.dojo.es.reservations.kafka.config.TopicNames
import de.vkb.dojo.es.reservations.model.command.ReservationCommand
import de.vkb.dojo.es.reservations.model.event.ReservationEvent
import de.vkb.dojo.es.reservations.model.state.Reservation
import io.micronaut.configuration.kafka.serde.JsonObjectSerde
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.json.JsonObjectSerializer
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.*

@Factory
class StreamFactory(
    val storeNames: StoreNames,
    val topicNames: TopicNames,
    val objectSerializer: JsonObjectSerializer
) {

    @Singleton
    fun eventAggregatorTopology(
        @Named("default") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        val events = builder.stream(
                topicNames.reservationEventInternal,
                Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java))
            )

        //TODO: die Event-Verarbeitung soll hier erfolgen.
        //TODO: wenn das Event nicht zum aktuellen Aggregat passt, soll ein FailFeedback unter der OperationId versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte das Event im eventExternal Topic versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte der neue State im stateTopic versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte das Event und der neue State im eventStateTopic versendet werden

        return events
    }

    @Singleton
    fun commandHandlerTopology(
        @Named("reservation-command-handler") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        val commands = builder.stream(
            topicNames.reservationCommand,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationCommand::class.java))
        )

        //TODO: die Verarbeitung von Commands zu Events, inkl. Plausibilitätsprüfung etc sollte hier erfolgen.

        //TODO: wenn kein Event erzeugt wurde, sollte ein Feedback Objekt in das feedback-Topic mit der operationId des commands als Key versendet werden

        return commands
    }

    @Singleton
    fun roomSagaTopology(
        @Named("reservation-room-saga") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        val roomEvents = builder.stream(
            topicNames.roomEvent,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, RoomEvent::class.java)),
        )

        //TODO: wenn ein Raum gelöscht oder gesperrt wird, alle bestehenden Reservierungen löschen

        return roomEvents
    }

    @Singleton
    fun personSagaTopology(
        @Named("reservation-person-saga") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        val personEvents =  builder.stream(
            topicNames.personEvent,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEvent::class.java)),
        )

        //TODO: wenn eine Person gelöscht oder krank wird, alle bestehenden Reservierungen löschen

        return personEvents
    }

    @Singleton
    fun personReaderTopology(
        @Named("reservation-reader") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        builder.globalTable(
            topicNames.reservationState,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, Reservation::class.java)),
            Materialized.`as`(storeNames.reservationReaderState)
        )

        return builder.stream(
            topicNames.reservationEventExternal, // we need to return a KStream, otherwise Micronaut does not start this topology.
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java)),
        )
    }

    @Singleton
    fun feedbackReaderTopology(
        @Named("reservation-feedback-reader") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        builder.globalTable(
            topicNames.feedback,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, Feedback::class.java)),
            Materialized.`as`(storeNames.feedbackReaderState)
        )

        return builder.stream(
            topicNames.reservationEventExternal, // we need to return a KStream, otherwise Micronaut does not start this topology.
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java)),
        )
    }
}
