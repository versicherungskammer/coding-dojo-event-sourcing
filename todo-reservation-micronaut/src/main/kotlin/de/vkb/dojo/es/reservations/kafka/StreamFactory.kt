package de.vkb.dojo.es.reservations.kafka

import de.vkb.dojo.es.common.model.feedback.FailFeedback
import de.vkb.dojo.es.common.model.feedback.Feedback
import de.vkb.dojo.es.common.model.feedback.SuccessFeedback
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent
import de.vkb.dojo.es.facilityManagement.model.state.Room
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.state.Person
import de.vkb.dojo.es.reservations.kafka.config.StoreNames
import de.vkb.dojo.es.reservations.kafka.config.TopicNames
import de.vkb.dojo.es.reservations.model.aggregate.ReservationAggregate
import de.vkb.dojo.es.reservations.model.command.CreateReservation
import de.vkb.dojo.es.reservations.model.command.DeleteReservation
import de.vkb.dojo.es.reservations.model.command.ReservationCommand
import de.vkb.dojo.es.reservations.model.event.ReservationCreated
import de.vkb.dojo.es.reservations.model.event.ReservationDeleted
import de.vkb.dojo.es.reservations.model.event.ReservationEvent
import de.vkb.dojo.es.reservations.model.eventState.ReservationEventAndState
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
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.Stores
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

        //TODO: die Event-Verarbeitung soll hier erfolgen.
        //TODO: wenn das Event nicht zum aktuellen Aggregat passt, soll ein FailFeedback unter der OperationId versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte das Event im eventExternal Topic versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte der neue State im stateTopic versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte das Event und der neue State im eventStateTopic versendet werden

        data class TransformerResult(
            val externalEvent: ReservationEvent?,
            val aggregate: ReservationAggregate?,
            val feedback: Feedback?,
            val operationId: String?,
        )

        builder.addStateStore(
            Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(storeNames.reservationEventAggregate),
                Serdes.StringSerde(), JsonObjectSerde(objectSerializer, ReservationAggregate::class.java)
            )
        )

        val transformedStream = builder.stream(
            topicNames.reservationEventInternal,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java))
        )
            .map { _, v -> KeyValue(v.aggregateId, v) }
            .repartition(Repartitioned.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java)))
            .transformValues(
                ValueTransformerWithKeySupplier {

                    object : ValueTransformerWithKey<String, ReservationEvent, TransformerResult> {

                        var aggregateStore: KeyValueStore<String, ReservationAggregate>? = null

                        override fun init(context: ProcessorContext?) {
                            aggregateStore = context?.getStateStore(storeNames.reservationEventAggregate)
                        }

                        override fun transform(readOnlyKey: String, event: ReservationEvent): TransformerResult =
                            when (event) {
                                is ReservationCreated -> {
                                    if (aggregateStore?.get(event.aggregateId) != null) TransformerResult(
                                        externalEvent = null,
                                        aggregate = null,
                                        feedback = FailFeedback("Reservation already exists"),
                                        operationId = event.operationId,
                                    )
                                    else {
                                        val aggregate = ReservationAggregate(
                                            event.aggregateId,
                                            Reservation(event.person, event.room)
                                        )
                                        aggregateStore?.put(event.aggregateId, aggregate)
                                        TransformerResult(
                                            externalEvent = event,
                                            aggregate = aggregate,
                                            feedback = SuccessFeedback(event.reference),
                                            operationId = null
                                        )
                                    }
                                }

                                is ReservationDeleted -> TransformerResult(null, null, null, null)
                                else -> TransformerResult(null, null, null, null)
                            }

                        override fun close() {}
                    }
                }, storeNames.reservationEventAggregate

            )

        transformedStream
            .filter { _, value -> value.feedback != null }
            .map { _, value -> KeyValue(value.operationId, value.feedback) }
            .to(
                topicNames.feedback,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, Feedback::class.java))
            )

        transformedStream
            .filter { _, value -> value.externalEvent != null }
            .map { _, value -> KeyValue(value.aggregate!!.id, value.externalEvent) }
            .to(
                topicNames.reservationEventExternal,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java))
            )

        transformedStream
            .filter { _, value -> value.aggregate != null }
            .map { _, value -> KeyValue(value.aggregate!!.id, value.aggregate.reservation) }
            .to(
                topicNames.reservationState,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, Reservation::class.java))
            )

        transformedStream
            .filter { _, value -> value.externalEvent != null && value.aggregate != null }
            .map { _, value ->
                KeyValue(
                    value.aggregate!!.id,
                    ReservationEventAndState(value.externalEvent!!, value.aggregate.reservation)
                )
            }
            .to(
                topicNames.reservationEventAndState,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEventAndState::class.java))
            )

        return transformedStream
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
        data class TransformerResult(
            val internalEvent: ReservationEvent?,
            val feedback: Feedback?,
            val operationId: String?
        )

        val transformedStream = builder.stream(
            topicNames.reservationCommand,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationCommand::class.java))
        )
            .transformValues(
                ValueTransformerWithKeySupplier {
                    object : ValueTransformerWithKey<String, ReservationCommand, TransformerResult> {

                        lateinit var roomStore: TimestampedKeyValueStore<String, Room>
                        lateinit var personStore: TimestampedKeyValueStore<String, Person>

                        override fun init(context: ProcessorContext) {
                            roomStore = context.getStateStore(storeNames.reservationCommandRooms)
                            personStore = context.getStateStore(storeNames.reservationCommandPersons)
                        }

                        override fun transform(
                            readOnlyKey: String,
                            command: ReservationCommand
                        ): TransformerResult =
                            when (command) {
                                is CreateReservation -> {
                                    val room = roomStore[command.room].value()
                                    val person = personStore[command.person].value()
                                    when {
                                        room == null -> TransformerResult(
                                            null,
                                            FailFeedback("Room doesn't exist"),
                                            command.operationId
                                        )

                                        room.maintenance -> TransformerResult(
                                            null,
                                            FailFeedback("Room under maintenance"),
                                            command.operationId
                                        )

                                        person == null -> TransformerResult(
                                            null,
                                            FailFeedback("Person doesn't exist"),
                                            command.operationId
                                        )

                                        person.sick -> TransformerResult(
                                            null,
                                            FailFeedback("Person is sick"),
                                            command.operationId
                                        )

                                        else -> TransformerResult(
                                            ReservationCreated(
                                                aggregateId = UUID.randomUUID().toString(),
                                                operationId = command.operationId,
                                                room = RoomData(command.room, room.name),
                                                person = PersonData(command.person, person.username, person.fullname)
                                            ), null, command.operationId
                                        )
                                    }
                                }

                                is DeleteReservation -> TransformerResult(null, null, null)
                                else -> TransformerResult(null, null, null)
                            }

                        override fun close() {}
                    }
                }
            )

        transformedStream
            .filter { _, value -> value.internalEvent != null }
            .map { _, value -> KeyValue(value.internalEvent!!.aggregateId, value.internalEvent) }
            .to(
                topicNames.reservationEventInternal,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, ReservationEvent::class.java))
            )

        transformedStream
            .filter { _, value -> value.feedback != null }
            .map { _, value -> KeyValue(value.operationId, value.feedback) }
            .to(
                topicNames.feedback,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, Feedback::class.java))
            )


        return transformedStream
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
