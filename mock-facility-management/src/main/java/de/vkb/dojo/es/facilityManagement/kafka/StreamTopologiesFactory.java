package de.vkb.dojo.es.facilityManagement.kafka;

import de.vkb.dojo.es.facilityManagement.kafka.config.StoreNames;
import de.vkb.dojo.es.facilityManagement.kafka.config.TopicNames;
import de.vkb.dojo.es.facilityManagement.model.aggregate.RoomAggregate;
import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.facilityManagement.model.eventState.RoomEventAndState;
import de.vkb.dojo.es.facilityManagement.model.feedback.Feedback;
import de.vkb.dojo.es.facilityManagement.model.state.Room;
import de.vkb.dojo.es.facilityManagement.serde.JsonSerdeFactory;
import de.vkb.dojo.es.facilityManagement.services.commands.CommandHandlerResult;
import de.vkb.dojo.es.facilityManagement.services.commands.delegating.DelegatingCommandHandler;
import de.vkb.dojo.es.facilityManagement.services.commands.delegating.PickyCommandHandler;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;
import de.vkb.dojo.es.facilityManagement.services.events.delegating.DelegatingEventAggregator;
import de.vkb.dojo.es.facilityManagement.services.events.delegating.PickyEventAggregator;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Repartitioned;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StreamTopologiesFactory {
    @Bean("roomCommandHandlerTopology")
    public Topology createRoomCommandTopology(
            TopicNames topicNames,
            JsonSerdeFactory jsonSerdeFactory,
            List<PickyCommandHandler<RoomCommand, ?, RoomEvent, ?>> handlers,
            @Qualifier("roomCommandHandlerTopologyBuilder") StreamsBuilder builder
    ) {
        var delegatingHandler = new DelegatingCommandHandler<>(handlers);

        var resultStream = builder.stream(
                        topicNames.getRoomCommand(),
                        Consumed.with(Serdes.String(), jsonSerdeFactory.of(RoomCommand.class))
                )
                .mapValues(delegatingHandler::handle);

        var successful = resultStream
                .filter( (k, v) -> v.getFeedback().getSuccess() );

        successful
                .mapValues(CommandHandlerResult::getEvent)
                .to(
                        topicNames.getRoomEventInternal(),
                        Produced.with(Serdes.String(), jsonSerdeFactory.of(RoomEvent.class))
                );

        resultStream
                .filter( (k, v) -> !v.getFeedback().getSuccess() )
                .map( (k, v) -> new KeyValue<>(v.getCommand().getOperationId(), v.getFeedback()))
                .to(
                        topicNames.getFeedback(),
                        Produced.with(Serdes.String(), jsonSerdeFactory.of(Feedback.class))
                );

        return builder.build();
    }

    @Bean("roomEventAggregatorTopology")
    public Topology createRoomEventTopology(
            TopicNames topicNames,
            StoreNames storeNames,
            JsonSerdeFactory jsonSerdeFactory,
            List<PickyEventAggregator<RoomEvent, ?, RoomAggregate>> aggregators,
            @Qualifier("roomEventAggregatorTopologyBuilder") StreamsBuilder builder
    ) {
        var delegatingAggregator = new DelegatingEventAggregator<>(aggregators);

        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(storeNames.roomEventAggregate),
                        Serdes.String(), jsonSerdeFactory.of(RoomAggregate.class)
                )
        );

        var resultStream = builder.stream(
                        topicNames.getRoomEventInternal(),
                        Consumed.with(Serdes.String(), jsonSerdeFactory.of(RoomEvent.class))
                )
                .map( (k, v) -> new KeyValue<>(v.getAggregateId(), v) )
                .repartition(
                        Repartitioned.with(
                                Serdes.String(), jsonSerdeFactory.of(RoomEvent.class)
                        )
                )
                .transformValues(
                        new EventAggregatorTransformerSupplier<>(storeNames.roomEventAggregate, delegatingAggregator),
                        storeNames.roomEventAggregate
                );

        var successful = resultStream
                .filter( (k, v) -> v.getFeedback().getSuccess() );

        successful
                .mapValues(EventAggregatorResult::getEvent)
                .to(
                        topicNames.getRoomEventExternal(),
                        Produced.with(Serdes.String(), jsonSerdeFactory.of(RoomEvent.class))
                );
        successful
                .mapValues( v -> v.getAggregate() != null ? v.getAggregate().getRoom() : null)
                .to(
                        topicNames.getRoomState(),
                        Produced.with(Serdes.String(), jsonSerdeFactory.of(Room.class))
                );
        successful
                .mapValues( v -> new RoomEventAndState(v.getEvent(), v.getAggregate() != null ? v.getAggregate().getRoom() : null))
                .to(
                        topicNames.getRoomEventAndState(),
                        Produced.with(Serdes.String(), jsonSerdeFactory.of(RoomEventAndState.class))
                );

        resultStream
                .map( (k, v) -> new KeyValue<>(v.getEvent().getOperationId(), v.getFeedback()))
                .to(
                        topicNames.getFeedback(),
                        Produced.with(Serdes.String(), jsonSerdeFactory.of(Feedback.class))
                );

        return builder.build();
    }

    @Bean("roomReaderTopology")
    public Topology createRoomReaderTopology(
            TopicNames topicNames,
            StoreNames storeNames,
            JsonSerdeFactory jsonSerdeFactory,
            @Qualifier("roomReaderTopologyBuilder") StreamsBuilder builder
    ) {
        builder.globalTable(
                topicNames.getRoomState(),
                Consumed.with(
                        Serdes.String(),
                        jsonSerdeFactory.of(Room.class)
                ),
                Materialized.as(storeNames.roomReaderState)
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
