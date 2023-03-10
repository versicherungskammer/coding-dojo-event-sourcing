package de.vkb.dojo.es.facilityManagement.kafka.config;

import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
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

    @Bean("roomCommandHandlerTopologyBuilder")
    public StreamsBuilderFactoryBean roomCommandHandlerTopologyBuilder(
        @Value("${spring.kafka.streams.roomCommandHandler.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean("roomEventAggregatorTopologyBuilder")
    public StreamsBuilderFactoryBean roomEventAggregatorTopologyBuilder(
        @Value("${spring.kafka.streams.roomEventAggregator.application.id}") String applicationId
    )  {
        return new StreamsBuilderFactoryBean(getStreamConfig(applicationId));
    }

    @Bean("roomReaderTopologyBuilder")
    public StreamsBuilderFactoryBean roomReaderTopologyBuilder(
        @Value("${spring.kafka.streams.roomReader.application.id}") String applicationId
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
    public KafkaTemplate<String, RoomCommand> roomCommandProducerTemplate(
        ProducerFactory<String, RoomCommand> producerFactory,
        @Value("${spring.kafka.producer.roomCommandProducer.clientId}") String clientId
    ) {
        producerFactory.updateConfigs(getProducerConfig(clientId));
        return new KafkaTemplate<String, RoomCommand>(producerFactory);
    }
}
