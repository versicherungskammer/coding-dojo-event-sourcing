package de.vkb.dojo.es.humanResources.kafka

import de.vkb.dojo.es.humanResources.model.feedback.Feedback
import io.micronaut.configuration.kafka.serde.JsonObjectSerde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.processor.ProcessorSupplier
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.StoreBuilder
import org.apache.kafka.streams.state.Stores

class StateStoreProcessorSupplier<V>(
    private val storeName: String,
    private val serde: Serde<V>
): ProcessorSupplier<String,V> {
    override fun get(): Processor<String, V> {
        return ProcessorImpl(storeName)
    }

    override fun stores(): MutableSet<StoreBuilder<*>> {
        return mutableSetOf(
            Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(storeName),
                Serdes.StringSerde(), serde
            )
        )
    }

    class ProcessorImpl<V>(
        private val storeName: String
    ): Processor<String, V> {
        private var store: KeyValueStore<String,V>? = null
        override fun init(context: ProcessorContext?) {
            store = context?.getStateStore(storeName)
        }

        override fun close() {
        }

        override fun process(key: String, value: V) {
            store?.put(key, value)
        }
    }
}
