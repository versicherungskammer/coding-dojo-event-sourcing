package de.vkb.dojo.es.facilityManagement.services.store;

import de.vkb.dojo.es.facilityManagement.kafka.config.StoreNames;
import de.vkb.dojo.es.facilityManagement.model.feedback.Feedback;
import de.vkb.dojo.es.facilityManagement.model.state.Room;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class RoomStore {
    final StoreNames storeNames;
    final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public RoomStore(StoreNames storeNames, @Qualifier("roomReaderTopologyBuilder") StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.storeNames = storeNames;
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    private ReadOnlyKeyValueStore<String, Room> getStore() {
        return streamsBuilderFactoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(storeNames.roomReaderState, QueryableStoreTypes.keyValueStore()));
    }

    public List<Room> getAll() {
        Iterable<KeyValue<String, Room>> iterable = getStore()::all;
        return StreamSupport.stream(iterable.spliterator(), false)
                .map( it -> it.value )
                .collect(Collectors.toList());
    }

    public Room get(String id) {
        return getStore().get(id);
    }
}
