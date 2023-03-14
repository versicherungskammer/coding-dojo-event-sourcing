package de.vkb.dojo.es.reservations.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;


@Configuration
class StreamBuilderFactoryConfig {
    @Value("${spring.kafka.bootstrapServers}")
    String bootstrapServers;

    @Value("${spring.kafka.streams.properties.processing.guarantee}")
    String processingGuarantee;

    @Value("${spring.kafka.streams.properties.auto-offset-reset}")
    String autoOffsets;

    @Value("${spring.kafka.streams.properties.replication-factor}")
    String replica;

    @Value("${spring.kafka.replayMode}")
    Boolean replayMode;


    private Map<String,Object> getBaseConfig() {
        var result = new HashMap<String,Object>();
        result.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return result;
    }
    private Map<String,Object> getProducerConfig(String clientId) {
        var result = getBaseConfig();
        result.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return result;
    }
    private KafkaStreamsConfiguration getStreamConfig(String applicationId) {
        var result = getBaseConfig();
        result.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        result.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, processingGuarantee);
        result.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsets);
        result.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replica);
        return new KafkaStreamsConfiguration(result);
    }

    @Bean("reservationCommandHandlerTopologyBuilder")
    public StreamsBuilderFactoryBean reservationommandHandlerTopologyBuilder(
        @Value("${spring.kafka.streams.reservationCommandHandler.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean("reservationEventAggregatorTopologyBuilder")
    public StreamsBuilderFactoryBean reservationEventAggregatorTopologyBuilder(
        @Value("${spring.kafka.streams.reservationEventAggregator.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean("reservationRoomSagaTopologyBuilder")
    public StreamsBuilderFactoryBean reservationRoomSagaTopologyBuilder(
        @Value("${spring.kafka.streams.reservationRoomSaga.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean("reservationPersonSagaTopologyBuilder")
    public StreamsBuilderFactoryBean reservationPersonSagaTopologyBuilder(
        @Value("${spring.kafka.streams.reservationPersonSaga.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean("reservationReaderTopologyBuilder")
    public StreamsBuilderFactoryBean reservationReaderTopologyBuilder(
        @Value("${spring.kafka.streams.reservationReader.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean("feedbackReaderTopologyBuilder")
    public StreamsBuilderFactoryBean feedbackReaderTopologyBuilder(
        @Value("${spring.kafka.streams.feedbackReader.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean
    public KafkaTemplate<String, de.vkb.dojo.es.reservations.model.command.ReservationCommand> reservationCommandProducerTemplate(
        ProducerFactory<String, de.vkb.dojo.es.reservations.model.command.ReservationCommand> producerFactory,
        @Value("${spring.kafka.producer.reeservationCommandProducer.clientId}") String clientId
    ) {
        producerFactory.updateConfigs(getProducerConfig(clientId));
        return new KafkaTemplate<String, de.vkb.dojo.es.reservations.model.command.ReservationCommand>(producerFactory);
    }
}
