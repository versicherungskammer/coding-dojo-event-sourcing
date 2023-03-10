package de.vkb.dojo.es.facilityManagement.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreNames {
    @Value("${spring.kafka.streams.roomEventAggregator.stores.aggregate}")
    public String roomEventAggregate;
    @Value("${spring.kafka.streams.roomReader.stores.state}")
    public String roomReaderState;
    @Value("${spring.kafka.streams.feedbackReader.stores.state}")
    public String feedbackReaderState;
}
