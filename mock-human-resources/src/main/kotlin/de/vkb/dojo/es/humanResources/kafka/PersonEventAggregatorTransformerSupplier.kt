package de.vkb.dojo.es.humanResources.kafka

import de.vkb.dojo.es.humanResources.kafka.config.StoreNames
import de.vkb.dojo.es.humanResources.model.aggregate.PersonAggregate
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.services.events.EventAggregator
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult
import de.vkb.dojo.es.humanResources.services.events.delegating.DelegatingEventAggregator
import de.vkb.dojo.es.humanResources.services.events.delegating.PickyEventAggregator
import jakarta.inject.Singleton
import org.apache.kafka.streams.kstream.ValueTransformerWithKey
import org.apache.kafka.streams.kstream.ValueTransformerWithKeySupplier
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.KeyValueStore

@Singleton
class PersonEventAggregatorTransformerSupplier(
    private val storeNames: StoreNames,
    private val aggregators: List<PickyEventAggregator<PersonEvent, *, PersonAggregate>>
): ValueTransformerWithKeySupplier<String, PersonEvent, EventAggregatorResult<PersonEvent, PersonAggregate>> {

    override fun get(): ValueTransformerWithKey<String, PersonEvent, EventAggregatorResult<PersonEvent, PersonAggregate>> {
        return Transformer(storeNames.personEventAggregate, DelegatingEventAggregator(aggregators))
    }

    class Transformer(
        private val aggregateStoreName: String,
        private val aggregator: EventAggregator<PersonEvent, PersonAggregate>
    ): ValueTransformerWithKey<String, PersonEvent, EventAggregatorResult<PersonEvent, PersonAggregate>> {
        var aggregateStore: KeyValueStore<String, PersonAggregate>? = null

        override fun init(context: ProcessorContext?) {
            aggregateStore = context?.getStateStore(aggregateStoreName)
        }

        override fun close() {
        }

        override fun transform(
            readOnlyKey: String?,
            value: PersonEvent?
        ): EventAggregatorResult<PersonEvent, PersonAggregate> {
            require(value != null) { "got null event" }

            val aggregate = aggregateStore?.get(value.aggregateId)

            val result = aggregator.process(value, aggregate)
            if (result.feedback.success) {
                aggregateStore?.put(value.aggregateId, result.aggregate)
            }

            return result
        }
    }
}
