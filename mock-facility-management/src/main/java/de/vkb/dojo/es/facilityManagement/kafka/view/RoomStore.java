package de.vkb.dojo.es.facilityManagement.kafka.view;

import de.vkb.dojo.es.facilityManagement.kafka.config.StoreNames;
import de.vkb.dojo.es.facilityManagement.model.dto.RoomOutput;
import de.vkb.dojo.es.facilityManagement.model.state.Room;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    public List<RoomOutput> getAll() {
        Iterable<KeyValue<String, Room>> iterable = getStore()::all;
        return StreamSupport.stream(iterable.spliterator(), false)
                .map( it -> new RoomOutput(it.key, it.value) )
                .collect(Collectors.toList());
    }

    public Optional<RoomOutput> get(String id) {
        return Optional.ofNullable(getStore().get(id))
                .map( it -> new RoomOutput(id, it) );
    }
}
