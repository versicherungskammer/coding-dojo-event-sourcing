package de.vkb.dojo.es.reservations.kafka;

import de.vkb.dojo.es.common.model.feedback.FailFeedback;
import de.vkb.dojo.es.common.model.feedback.Feedback;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.state.Room;
import de.vkb.dojo.es.humanResources.model.event.PersonEvent;
import de.vkb.dojo.es.humanResources.model.state.Person;
import de.vkb.dojo.es.reservations.kafka.config.StoreNames;
import de.vkb.dojo.es.reservations.kafka.config.TopicNames;
import de.vkb.dojo.es.reservations.kafka.validation.CommandResult;
import de.vkb.dojo.es.reservations.kafka.validation.EventResult;
import de.vkb.dojo.es.reservations.model.command.CreateReservation;
import de.vkb.dojo.es.reservations.model.command.ReservationCommand;
import de.vkb.dojo.es.reservations.model.event.ReservationCreated;
import de.vkb.dojo.es.reservations.model.event.ReservationEvent;
import de.vkb.dojo.es.reservations.model.eventState.ReservationEventAndState;
import de.vkb.dojo.es.reservations.model.state.Reservation;
import de.vkb.dojo.es.reservations.model.view.PersonData;
import de.vkb.dojo.es.reservations.model.view.RoomData;
import de.vkb.dojo.es.reservations.serde.JsonSerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.TimestampedKeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StreamTopologiesFactory {

    private final TopicNames topicNames;
    private final StoreNames storeNames;
    private final JsonSerdeFactory jsonSerdeFactory;

    public StreamTopologiesFactory(TopicNames topicNames, StoreNames storeNames, JsonSerdeFactory jsonSerdeFactory) {
        this.topicNames = topicNames;
        this.storeNames = storeNames;
        this.jsonSerdeFactory = jsonSerdeFactory;
    }

    @Bean("reservationEventAggregatorTopology")
    public Topology createRoomEventTopology(
            @Qualifier("reservationEventAggregatorTopologyBuilder") StreamsBuilder builder
    ) {

        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(storeNames.reservationReaderState),
                        Serdes.String(), jsonSerdeFactory.of(Reservation.class)
                )
        );

        var events = builder.stream(
                topicNames.getReservationEventInternal(),
                Consumed.with(Serdes.String(), jsonSerdeFactory.of(ReservationEvent.class))
        );

        var createdEvents = events.filter((key, value) -> value instanceof ReservationCreated)
                .transformValues(() -> new ValueTransformerWithKey<String, ReservationEvent, EventResult>() {
                    private KeyValueStore<String, Reservation> reservationStore;

                    @Override
                    public void init(ProcessorContext context) {
                        this.reservationStore = context.getStateStore(storeNames.reservationReaderState);
                    }

                    @Override
                    public EventResult transform(String readOnlyKey, ReservationEvent value) {
                        var reservationExists = reservationStore.get(readOnlyKey) != null;
                        var event = (ReservationCreated) value;

                        if (reservationExists) {
                            return new EventResult(new FailFeedback("Reservation already exists"), event.getOperationId());
                        } else {
                            var reservation = new Reservation(event.getRoom(), event.getPerson());
                            reservationStore.put(readOnlyKey, reservation);
                            return new EventResult(event, reservation);
                        }
                    }

                    @Override
                    public void close() {

                    }
                }, storeNames.reservationReaderState);

        createdEvents.filter((key, value) -> value.getFeedback() != null)
                .map((key, value) -> new KeyValue<>(value.getOperationId(), value.getFeedback()))
                .to(topicNames.getFeedback(), Produced.with(Serdes.String(), jsonSerdeFactory.of(Feedback.class)));

        createdEvents.filter((key, value) -> value.getEvent() != null)
                .map((key, value) -> new KeyValue<>(value.getEvent().getAggregateId(), value.getEvent()))
                .to(topicNames.getReservationEventExternal(), Produced.with(Serdes.String(), jsonSerdeFactory.of(ReservationEvent.class)));

        createdEvents.filter((key, value) -> value.getReservation() != null)
                .map((key, value) -> new KeyValue<>(value.getEvent().getAggregateId(), value.getReservation()))
                .to(topicNames.getReservationState(), Produced.with(Serdes.String(), jsonSerdeFactory.of(Reservation.class)));

        createdEvents.filter((key, value) -> value.getEvent() != null && value.getReservation() != null)
                .map((key, value) -> new KeyValue<>(value.getEvent().getAggregateId(), new ReservationEventAndState(value.getEvent(), value.getReservation())))
                .to(topicNames.getReservationEventAndState(), Produced.with(Serdes.String(), jsonSerdeFactory.of(ReservationEventAndState.class)));

        return builder.build();
    }

    @Bean("reservationCommandHandlerTopology")
    public Topology createRoomCommandTopology(
            @Qualifier("reservationCommandHandlerTopologyBuilder") StreamsBuilder builder
    ) {
        builder.globalTable(
                topicNames.getRoomState(), Consumed.with(Serdes.String(), jsonSerdeFactory.of(Room.class)),
                Materialized.as(storeNames.reservationCommandRooms));

        builder.globalTable(
                topicNames.getPersonState(), Consumed.with(Serdes.String(), jsonSerdeFactory.of(Person.class)),
                Materialized.as(storeNames.reservationCommandPersons));

        var commands = builder.stream(
                topicNames.getReservationCommand(),
                Consumed.with(Serdes.String(), jsonSerdeFactory.of(ReservationCommand.class))
        );

        var createCommand = commands.filter((key, value) -> value instanceof CreateReservation)
                .transformValues(() -> new ValueTransformerWithKey<String, ReservationCommand, CommandResult>() {
                    private TimestampedKeyValueStore<String, Room> roomStore;
                    private TimestampedKeyValueStore<String, Person> personStore;

                    @Override
                    public void init(ProcessorContext context) {
                        roomStore = context.getStateStore(storeNames.reservationCommandRooms);
                        personStore = context.getStateStore(storeNames.reservationCommandPersons);
                    }

                    @Override
                    public CommandResult transform(String readOnlyKey, ReservationCommand value) {
                        var createCommand = (CreateReservation) value;
                        var person = personStore.get(createCommand.getPerson()).value();
                        var room = roomStore.get(createCommand.getRoom()).value();

                        if (person != null && room != null) {
                            if (person.getSick() && room.getMaintenance()) {
                                return new CommandResult(new FailFeedback("Person is sick and room is under maintenance"), null, value.getOperationId());
                            } else if (person.getSick()) {
                                return new CommandResult(new FailFeedback("Person is sick"), null, value.getOperationId());
                            } else if (room.getMaintenance()) {
                                return new CommandResult(new FailFeedback("Room is under maintenance"), null, value.getOperationId());
                            } else {
                                var event = new ReservationCreated(
                                        value.getOperationId(),
                                        value.getAggregateId(),
                                        new RoomData(createCommand.getRoom(), room),
                                        new PersonData(createCommand.getPerson(), person)
                                );
                                return new CommandResult(null, event, value.getOperationId());
                            }
                        } else if (person == null && room != null) {
                            return new CommandResult(new FailFeedback("Person does not exist"), null, value.getOperationId());
                        } else if (person != null) {
                            return new CommandResult(new FailFeedback("Room does not exist"), null, value.getOperationId());
                        } else {
                            return new CommandResult(new FailFeedback("Room and Person do not exist"), null, value.getOperationId());
                        }
                    }

                    @Override
                    public void close() {

                    }
                });

        createCommand.filter((key, value) -> value.getEvent() != null)
                .map((key, value) -> new KeyValue<>(value.getEvent().getAggregateId(), value.getEvent()))
                .to(topicNames.getReservationEventInternal(), Produced.with(Serdes.String(), jsonSerdeFactory.of(ReservationEvent.class)));

        createCommand.filter((key, value) -> value.getFeedback() != null)
                .map((key, value) -> new KeyValue<>(value.getOperationId(), value.getFeedback()))
                .to(topicNames.getFeedback(), Produced.with(Serdes.String(), jsonSerdeFactory.of(Feedback.class)));

        return builder.build();
    }

    @Bean("reservationRoomSagaTopology")
    public Topology createReservationRoomSagaTopology(
            @Qualifier("reservationRoomSagaTopologyBuilder") StreamsBuilder builder
    ) {
        var roomEvents = builder.stream(
                topicNames.getRoomEvent(),
                Consumed.with(Serdes.String(), jsonSerdeFactory.of(RoomEvent.class))
        );

        //TODO: wenn ein Raum gelöscht oder gesperrt wird, alle bestehenden Reservierungen löschen

        return builder.build();
    }

    @Bean("reservationPersonSagaTopology")
    public Topology createReservationPersonSagaTopology(
            @Qualifier("reservationPersonSagaTopologyBuilder") StreamsBuilder builder
    ) {
        var personEvents = builder.stream(
                topicNames.getPersonEvent(),
                Consumed.with(Serdes.String(), jsonSerdeFactory.of(PersonEvent.class))
        );

        //TODO: wenn eine Person gelöscht oder krank wird, alle bestehenden Reservierungen löschen

        return builder.build();
    }

    @Bean("reservationReaderTopology")
    public Topology createRoomReaderTopology(
            @Qualifier("reservationReaderTopologyBuilder") StreamsBuilder builder
    ) {
        builder.globalTable(
                topicNames.getReservationState(),
                Consumed.with(
                        Serdes.String(),
                        jsonSerdeFactory.of(Reservation.class)
                ),
                Materialized.as(storeNames.reservationReaderState)
        );

        return builder.build();
    }

    @Bean("feedbackReaderTopology")
    public Topology createFeedbackReaderTopology(
            @Qualifier("feedbackReaderTopologyBuilder") StreamsBuilder builder
    ) {
        builder.globalTable(
                topicNames.getFeedback(),
                Consumed.with(
                        Serdes.String(),
                        jsonSerdeFactory.of(Feedback.class)
                ),
                Materialized.as(storeNames.feedbackReaderState)
        );

        return builder.build();
    }
}
