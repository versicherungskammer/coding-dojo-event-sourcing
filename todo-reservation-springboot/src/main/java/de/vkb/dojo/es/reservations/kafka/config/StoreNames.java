package de.vkb.dojo.es.reservations.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreNames {
    @Value("${spring.kafka.streams.reservationCommandHandler.stores.rooms}")
    public String reservationCommandRooms;
    @Value("${spring.kafka.streams.reservationCommandHandler.stores.persons}")
    public String reservationCommandPersons;
    @Value("${spring.kafka.streams.reservationEventAggregator.stores.aggregate}")
    public String reservationEventAggregate;
    @Value("${spring.kafka.streams.reservationRoomSaga.stores.rooms}")
    public String reservationRoomSagaRooms;
    @Value("${spring.kafka.streams.reservationPersonSaga.stores.persons}")
    public String reservationPersonSagaPersons;
    @Value("${spring.kafka.streams.reservationReader.stores.state}")
    public String reservationReaderState;
    @Value("${spring.kafka.streams.feedbackReader.stores.state}")
    public String feedbackReaderState;
}
