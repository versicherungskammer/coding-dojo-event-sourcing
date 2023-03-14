package de.vkb.dojo.es.reservations.kafka;

import de.vkb.dojo.es.common.model.feedback.Feedback;
import de.vkb.dojo.es.reservations.kafka.config.StoreNames;
import de.vkb.dojo.es.reservations.kafka.config.TopicNames;
import de.vkb.dojo.es.reservations.model.aggregate.ReservationAggregate;
import de.vkb.dojo.es.reservations.model.state.Reservation;
import de.vkb.dojo.es.reservations.serde.JsonSerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StreamTopologiesFactory {
    @Bean("reservationCommandHandlerTopology")
    public Topology createRoomCommandTopology(
            TopicNames topicNames,
            JsonSerdeFactory jsonSerdeFactory,
            @Qualifier("reservationCommandHandlerTopologyBuilder") StreamsBuilder builder
    ) {
        var resultStream = builder.stream(
                        topicNames.getReservationCommand(),
                        Consumed.with(Serdes.String(), jsonSerdeFactory.of(de.vkb.dojo.es.reservations.model.command.ReservationCommand.class))
                );

        return builder.build();
    }

    @Bean("reservationEventAggregatorTopology")
    public Topology createRoomEventTopology(
            TopicNames topicNames,
            StoreNames storeNames,
            JsonSerdeFactory jsonSerdeFactory,
            @Qualifier("reservationEventAggregatorTopologyBuilder") StreamsBuilder builder
    ) {
        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(storeNames.reservationEventAggregate),
                        Serdes.String(), jsonSerdeFactory.of(ReservationAggregate.class)
                )
        );

        return builder.build();
    }

    @Bean("reservationRoomSagaTopology")
    public Topology createReservationRoomSagaTopology(
            TopicNames topicNames,
            StoreNames storeNames,
            JsonSerdeFactory jsonSerdeFactory,
            @Qualifier("reservationRoomSagaTopologyBuilder") StreamsBuilder builder
    ) {
        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(storeNames.reservationEventAggregate),
                        Serdes.String(), jsonSerdeFactory.of(ReservationAggregate.class)
                )
        );

        return builder.build();
    }

    @Bean("reservationPersonSagaTopology")
    public Topology createReservationPersonSagaTopology(
            TopicNames topicNames,
            StoreNames storeNames,
            JsonSerdeFactory jsonSerdeFactory,
            @Qualifier("reservationPersonSagaTopologyBuilder") StreamsBuilder builder
    ) {
        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(storeNames.reservationEventAggregate),
                        Serdes.String(), jsonSerdeFactory.of(ReservationAggregate.class)
                )
        );

        return builder.build();
    }

    @Bean("reservationReaderTopology")
    public Topology createRoomReaderTopology(
            TopicNames topicNames,
            StoreNames storeNames,
            JsonSerdeFactory jsonSerdeFactory,
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
            TopicNames topicNames,
            StoreNames storeNames,
            JsonSerdeFactory jsonSerdeFactory,
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
