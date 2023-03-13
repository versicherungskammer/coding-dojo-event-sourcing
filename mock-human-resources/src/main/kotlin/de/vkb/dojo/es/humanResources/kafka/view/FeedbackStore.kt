package de.vkb.dojo.es.humanResources.kafka.view

import de.vkb.dojo.es.humanResources.kafka.config.StoreNames
import de.vkb.dojo.es.humanResources.model.dto.PersonOutput
import de.vkb.dojo.es.humanResources.model.feedback.Feedback
import de.vkb.dojo.es.humanResources.model.state.Person
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
