package de.vkb.dojo.es.humanResources.kafka.view

import de.vkb.dojo.es.humanResources.kafka.config.StoreNames
import de.vkb.dojo.es.humanResources.model.dto.PersonOutput
import de.vkb.dojo.es.humanResources.model.state.Person
import io.micronaut.configuration.kafka.streams.InteractiveQueryService
import jakarta.inject.Singleton
import org.apache.kafka.streams.state.QueryableStoreTypes
import java.util.*

@Singleton
class PersonStore(
    private val query: InteractiveQueryService,
    private val storeNames: StoreNames
) {
    fun all(): List<PersonOutput> = query
        .getQueryableStore(storeNames.personReaderState, QueryableStoreTypes.keyValueStore<String, Person>())
        .map { it.all() }
        .map { it.asSequence() }
        .map { it.map { e -> PersonOutput(e.key, e.value) } }
        .map { it.toList() }
        .orElse(emptyList())

    fun get(id: String): Optional<PersonOutput> = query
        .getQueryableStore(storeNames.personReaderState, QueryableStoreTypes.keyValueStore<String, Person>())
        .flatMap { Optional.ofNullable(it[id]) }
        .map { PersonOutput(id, it) }

}
