package de.vkb.dojo.es.reservations.kafka;

import de.vkb.dojo.es.common.model.feedback.Feedback;
import de.vkb.dojo.es.facilityManagement.model.event.RoomEvent;
import de.vkb.dojo.es.humanResources.model.event.PersonEvent;
import de.vkb.dojo.es.reservations.kafka.config.StoreNames;
import de.vkb.dojo.es.reservations.kafka.config.TopicNames;
import de.vkb.dojo.es.reservations.model.command.ReservationCommand;
import de.vkb.dojo.es.reservations.model.event.ReservationEvent;
import de.vkb.dojo.es.reservations.model.state.Reservation;
import de.vkb.dojo.es.reservations.serde.JsonSerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
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
        var events = builder.stream(
                topicNames.getReservationEventInternal(),
                Consumed.with(Serdes.String(), jsonSerdeFactory.of(ReservationEvent.class))
        );

        //TODO: die Event-Verarbeitung soll hier erfolgen.
        //TODO: wenn das Event nicht zum aktuellen Aggregat passt, soll ein FailFeedback unter der OperationId versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte das Event im eventExternal Topic versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte der neue State im stateTopic versendet werden
        //TODO: wenn das Event erfolgreich verarbeitet werden konnte, sollte das Event und der neue State im eventStateTopic versendet werden

        return builder.build();
    }

    @Bean("reservationCommandHandlerTopology")
    public Topology createRoomCommandTopology(
            @Qualifier("reservationCommandHandlerTopologyBuilder") StreamsBuilder builder
    ) {
        var commands = builder.stream(
                topicNames.getReservationCommand(),
                Consumed.with(Serdes.String(), jsonSerdeFactory.of(ReservationCommand.class))
        );

        //TODO: die Verarbeitung von Commands zu Events, inkl. Plausibilitätsprüfung etc sollte hier erfolgen.

        //TODO: wenn kein Event erzeugt wurde, sollte ein Feedback Objekt in das feedback-Topic mit der operationId des commands als Key versendet werden

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
