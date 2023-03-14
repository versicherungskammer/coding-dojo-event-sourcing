package de.vkb.dojo.es.reservations.kafka.view

import de.vkb.dojo.es.common.model.feedback.Feedback
import de.vkb.dojo.es.reservations.kafka.config.StoreNames
import io.micronaut.configuration.kafka.streams.InteractiveQueryService
import jakarta.inject.Singleton
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.state.QueryableStoreTypes
import java.util.*

@Singleton
class FeedbackStore(
    private val query: InteractiveQueryService,
    private val storeNames: StoreNames
) {
    fun all(): List<KeyValue<String, Feedback>> = query
        .getQueryableStore(storeNames.feedbackReaderState, QueryableStoreTypes.keyValueStore<String, Feedback>())
        .map { it.all() }
        .map { it.asSequence() }
        .map { it.toList() }
        .orElse(emptyList())

    fun get(id: String): Optional<Feedback> = query
        .getQueryableStore(storeNames.feedbackReaderState, QueryableStoreTypes.keyValueStore<String, Feedback>())
        .flatMap { Optional.ofNullable(it[id]) }
}
