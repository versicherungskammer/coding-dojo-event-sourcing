package de.vkb.dojo.es.facilityManagement.kafka;


import de.vkb.dojo.es.facilityManagement.model.event.Event;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregator;
import de.vkb.dojo.es.facilityManagement.services.events.EventAggregatorResult;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.kstream.ValueTransformerWithKeySupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class EventAggregatorTransformerSupplier<E extends Event, A> implements ValueTransformerWithKeySupplier<String, E, EventAggregatorResult<E, A>> {
    private final String aggregateStoreName;
    private final EventAggregator<E, A> eventAggregator;

    public EventAggregatorTransformerSupplier(String aggregateStoreName, EventAggregator<E, A> eventAggregator) {
        this.aggregateStoreName = aggregateStoreName;
        this.eventAggregator = eventAggregator;
    }

    @Override
    public ValueTransformerWithKey<String, E, EventAggregatorResult<E, A>> get() {
        return new Transformer<E, A>(aggregateStoreName, eventAggregator);
    }

    static class Transformer<E extends Event, A> implements ValueTransformerWithKey<String, E, EventAggregatorResult<E, A>> {
        private final String aggregateStoreName;
        private final EventAggregator<E, A> eventAggregator;

        private KeyValueStore<String, A> aggregateStore = null;

        public Transformer(String aggregateStoreName, EventAggregator<E, A> eventAggregator) {
            this.aggregateStoreName = aggregateStoreName;
            this.eventAggregator = eventAggregator;
        }

        @Override
        public void init(ProcessorContext context) {
            aggregateStore = context.getStateStore(aggregateStoreName);
        }

        @Override
        public EventAggregatorResult<E, A> transform(String readOnlyKey, E value) {
            A aggregate = null;
            if (aggregateStore != null) {
                aggregate = aggregateStore.get(value.getAggregateId());
            }
            var result = eventAggregator.process(value, aggregate);
            if (result.getFeedback().getSuccess() && aggregateStore != null) {
                aggregateStore.put(value.getAggregateId(), result.getAggregate());
            }

            return result;
        }

        @Override
        public void close() {
        }
    }
}
