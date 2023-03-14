package de.vkb.dojo.es.reservations.kafka.view;

import de.vkb.dojo.es.common.model.feedback.Feedback;
import de.vkb.dojo.es.reservations.kafka.config.StoreNames;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class FeedbackStore {
    final StoreNames storeNames;
    final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public FeedbackStore(StoreNames storeNames, @Qualifier("feedbackReaderTopologyBuilder") StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.storeNames = storeNames;
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    private ReadOnlyKeyValueStore<String, Feedback> getStore() {
        return streamsBuilderFactoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(storeNames.feedbackReaderState, QueryableStoreTypes.keyValueStore()));
    }

    public Feedback get(String id) {
        return getStore().get(id);
    }
}
