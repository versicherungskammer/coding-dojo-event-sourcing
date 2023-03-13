package de.vkb.dojo.es.humanResources.kafka

import de.vkb.dojo.es.humanResources.kafka.config.StoreNames
import de.vkb.dojo.es.humanResources.kafka.config.TopicNames
import de.vkb.dojo.es.humanResources.model.aggregate.PersonAggregate
import de.vkb.dojo.es.humanResources.model.command.PersonCommand
import de.vkb.dojo.es.humanResources.model.event.PersonEvent
import de.vkb.dojo.es.humanResources.model.eventState.PersonEventAndState
import de.vkb.dojo.es.humanResources.model.feedback.Feedback
import de.vkb.dojo.es.humanResources.model.state.Person
import de.vkb.dojo.es.humanResources.services.commands.CommandHandlerResult
import de.vkb.dojo.es.humanResources.services.commands.delegating.DelegatingCommandHandler
import de.vkb.dojo.es.humanResources.services.commands.delegating.PickyCommandHandler
import de.vkb.dojo.es.humanResources.services.events.EventAggregatorResult
import io.micronaut.configuration.kafka.serde.JsonObjectSerde
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.json.JsonObjectSerializer
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.Stores

@Factory
class StreamFactory(
    val storeNames: StoreNames,
    val topicNames: TopicNames,
    val objectSerializer: JsonObjectSerializer
) {

    @Singleton
    fun eventAggregatorTopology(
        @Named("default") builder: ConfiguredStreamBuilder,
        tranformerSupplier: PersonEventAggregatorTransformerSupplier
    ): KStream<String, *> {
        builder.addStateStore(
            Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(storeNames.personEventAggregate),
                Serdes.StringSerde(), JsonObjectSerde(objectSerializer, PersonAggregate::class.java)
            )
        )

        val resultStream = builder.stream(
                topicNames.personEventInternal,
                Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEvent::class.java))
            )
            .map { _, v -> KeyValue(v.aggregateId, v) }
            .repartition(Repartitioned.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEvent::class.java)))
            .transformValues(tranformerSupplier, storeNames.personEventAggregate)

        val successful = resultStream
            .filter { _, v -> v.feedback.success }

        successful
            .mapValues { v -> v.event }
            .to(
                topicNames.personEventExternal,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEvent::class.java))
            )

        successful
            .mapValues { v -> PersonEventAndState(v.event, v.aggregate?.person) }
            .to(
                topicNames.personEventAndState,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEventAndState::class.java))
            )

        successful
            .mapValues { v -> v.aggregate?.person }
            .to(
                topicNames.personState,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, Person::class.java))
            )

        resultStream
            .map { _, v -> KeyValue(v.event.operationId, v.feedback) }
            .to(
                topicNames.feedback,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, Feedback::class.java))
            )

        resultStream
            .filter { _, v -> !v.feedback.success }
            .map { _, v -> KeyValue(v.event.operationId, v) }
            .print(
                Printed.toSysOut<String?, EventAggregatorResult<PersonEvent, PersonAggregate>>()
                    .withLabel("event failed")
            )

        successful
            .map { _, v -> KeyValue(v.event.operationId, v) }
            .print(
                Printed.toSysOut<String?, EventAggregatorResult<PersonEvent, PersonAggregate>>()
                    .withLabel("event processed")
            )

        return successful
    }

    @Singleton
    fun commandHandlerTopology(
        @Named("person-command-handler") builder: ConfiguredStreamBuilder,
        commandHandlers: List<PickyCommandHandler<PersonCommand, *, PersonEvent, *>>
    ): KStream<String, *> {
        val commandHandler = DelegatingCommandHandler(commandHandlers)

        val resultStream = builder.stream(
                topicNames.personCommand,
                Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonCommand::class.java))
            )
            .mapValues { _, v -> commandHandler.handle(v) }

        val successful = resultStream
            .filter { _, v -> v.feedback.success && v.event != null }

        successful
            .map { _, v -> KeyValue(v.event!!.aggregateId, v.event) }
            .to(
                topicNames.personEventInternal,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEvent::class.java))
            )

        val failed = resultStream
            .filter { _, v -> !v.feedback.success }

        failed
            .map { _, v -> KeyValue(v.command.operationId, v) }
            .print(
                Printed.toSysOut<String?, CommandHandlerResult<PersonCommand, PersonEvent>>()
                    .withLabel("command failed")
            )

        failed
            .map { _, v -> KeyValue(v.command.operationId, v.feedback) }
            .to(
                topicNames.feedback,
                Produced.with(Serdes.String(), JsonObjectSerde(objectSerializer, Feedback::class.java))
            )

        return successful
    }

    @Singleton
    fun personReaderTopology(
        @Named("person-reader") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        builder.globalTable(
            topicNames.personState,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, Person::class.java)),
            Materialized.`as`(storeNames.personReaderState)
        )

        return builder.stream(
            topicNames.personEventExternal, // we need to return a KStream, otherwise Micronaut does not start this topology.
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEvent::class.java)),
        )
    }

    @Singleton
    fun feedbackReaderTopology(
        @Named("person-feedback-reader") builder: ConfiguredStreamBuilder
    ): KStream<String, *> {
        builder.globalTable(
            topicNames.feedback,
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, Feedback::class.java)),
            Materialized.`as`(storeNames.feedbackReaderState)
        )

        return builder.stream(
            topicNames.personEventExternal, // we need to return a KStream, otherwise Micronaut does not start this topology.
            Consumed.with(Serdes.String(), JsonObjectSerde(objectSerializer, PersonEvent::class.java)),
        )
    }
}
