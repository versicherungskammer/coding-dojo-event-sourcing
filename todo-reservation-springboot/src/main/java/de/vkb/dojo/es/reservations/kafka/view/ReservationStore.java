package de.vkb.dojo.es.reservations.kafka.view;

import de.vkb.dojo.es.reservations.kafka.config.StoreNames;
import de.vkb.dojo.es.reservations.model.dto.ReservationOutput;
import de.vkb.dojo.es.reservations.model.state.Reservation;
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
public class ReservationStore {
    final StoreNames storeNames;
    final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public ReservationStore(StoreNames storeNames, @Qualifier("reservationReaderTopologyBuilder") StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.storeNames = storeNames;
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    private ReadOnlyKeyValueStore<String, Reservation> getStore() {
        return streamsBuilderFactoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(storeNames.reservationReaderState, QueryableStoreTypes.keyValueStore()));
    }

    public List<ReservationOutput> getAll() {
        Iterable<KeyValue<String, Reservation>> iterable = getStore()::all;
        return StreamSupport.stream(iterable.spliterator(), false)
                .map( it -> new ReservationOutput(it.key, it.value) )
                .collect(Collectors.toList());
    }

    public Optional<ReservationOutput> get(String id) {
        return Optional.ofNullable(getStore().get(id))
                .map( it -> new ReservationOutput(id, it) );
    }
}
