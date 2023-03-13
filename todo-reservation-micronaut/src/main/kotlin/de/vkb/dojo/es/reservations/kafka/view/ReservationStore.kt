package de.vkb.dojo.es.reservations.kafka.view

import de.vkb.dojo.es.reservations.kafka.config.StoreNames
import de.vkb.dojo.es.reservations.model.dto.ReservationOutput
import de.vkb.dojo.es.reservations.model.state.Reservation
import io.micronaut.configuration.kafka.streams.InteractiveQueryService
import jakarta.inject.Singleton
import org.apache.kafka.streams.state.QueryableStoreTypes
import java.util.*

@Singleton
class ReservationStore(
    private val query: InteractiveQueryService,
    private val storeNames: StoreNames
) {
    fun all(): List<ReservationOutput> = query
        .getQueryableStore(storeNames.reservationReaderState, QueryableStoreTypes.keyValueStore<String, Reservation>())
        .map { it.all() }
        .map { it.asSequence() }
        .map { it.map { e -> ReservationOutput(e.key, e.value) } }
        .map { it.toList() }
        .orElse(emptyList())

    fun get(id: String): Optional<ReservationOutput> = query
        .getQueryableStore(storeNames.reservationReaderState, QueryableStoreTypes.keyValueStore<String, Reservation>())
        .flatMap { Optional.ofNullable(it[id]) }
        .map { ReservationOutput(id, it) }

}
