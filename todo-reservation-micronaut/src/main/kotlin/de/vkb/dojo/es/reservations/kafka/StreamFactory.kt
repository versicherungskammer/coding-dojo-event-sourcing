package de.vkb.dojo.es.reservations.kafka

import de.vkb.dojo.es.common.model.event.Event
import de.vkb.dojo.es.common.model.feedback.FailFeedback
import de.vkb.dojo.es.common.model.feedback.Feedback
import de.vkb.dojo.es.common.model.feedback.SuccessFeedback
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent
import de.vkb.dojo.es.facilityManagement.model.state.Room
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.state.Person
import de.vkb.dojo.es.reservations.kafka.config.StoreNames
import de.vkb.dojo.es.reservations.kafka.config.TopicNames
import de.vkb.dojo.es.reservations.model.command.Command
import de.vkb.dojo.es.reservations.model.command.CreateReservation
import de.vkb.dojo.es.reservations.model.command.ReservationCommand
import de.vkb.dojo.es.reservations.model.event.ReservationCreated
import de.vkb.dojo.es.reservations.model.event.ReservationEvent
import de.vkb.dojo.es.reservations.model.state.Reservation
import de.vkb.dojo.es.reservations.model.view.PersonData
import de.vkb.dojo.es.reservations.model.view.RoomData
import io.micronaut.configuration.kafka.serde.JsonObjectSerde
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.json.JsonObjectSerializer
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.apache.kafka.streams.state.TimestampedKeyValueStore
import java.util.UUID

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

        //TODO: die Verarbeitung von Commands zu Events, inkl. Plausibilitätsprüfung etc sollte hier erfolgen.
        //TODO: wenn kein Event erzeugt wurde, sollte ein Feedback Objekt in das feedback-Topic mit der operationId des commands als Key versendet werden

        builder.globalTable(
            topicNames.roomState,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, Room::class.java)),
            Materialized.`as`(storeNames.reservationCommandRooms)
        )

        builder.globalTable(
            topicNames.personState,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, Person::class.java)),
            Materialized.`as`(storeNames.reservationCommandPersons)
        )
        data class EventFeedback(val event: ReservationEvent?, val feedback: Feedback?)

        val stream = builder.stream(
            topicNames.reservationCommand,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationCommand::class.java))
        )
            .transformValues(
                ValueTransformerWithKeySupplier {
                    object : ValueTransformerWithKey<String, ReservationCommand, EventFeedback> {

                        lateinit var roomStore: TimestampedKeyValueStore<String, Room>
                        lateinit var personStore: TimestampedKeyValueStore<String, Person>

                        override fun init(context: ProcessorContext) {
                            roomStore = context.getStateStore(storeNames.reservationCommandRooms)
                            personStore = context.getStateStore(storeNames.reservationCommandPersons)
                        }

                        override fun transform(
                            readOnlyKey: String,
                            command: ReservationCommand
                        ): EventFeedback =
                            when (command) {
                                is CreateReservation -> {
                                    val room = roomStore[command.room].value()
                                    val person = personStore[command.person].value()
                                    when {
                                        room == null -> EventFeedback(null, FailFeedback("Room doesn't exist"))
                                        room.maintenance -> EventFeedback(null, FailFeedback("Room under maintenance"))
                                        person == null -> EventFeedback(null, FailFeedback("Person doesn't exist"))
                                        person.sick -> EventFeedback(null, FailFeedback("Person is sick"))
                                        else -> EventFeedback(
                                            ReservationCreated(
                                                aggregateId = UUID.randomUUID().toString(),
                                                operationId = command.operationId,
                                                room = RoomData(command.room, room.name),
                                                person = PersonData(command.person, person.username, person.fullname)
                                            ), null
                                        )
                                    }
                                }
                                // todo: handle other Command types
                                else -> EventFeedback(null, null)
                            }
                        override fun close() {}
                    }
                }
            )

        stream
            .filter { _, value -> value.event != null }
            .map { _, value -> KeyValue(value.event!!.aggregateId, value.event) }
            .to (
                topicNames.reservationEventInternal,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java))
            )

        stream
            .filter { _, value -> value.feedback != null }
            .to(
                topicNames.feedback,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, EventFeedback::class.java))
            )


        return stream
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
        val personEvents = builder.stream(
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
