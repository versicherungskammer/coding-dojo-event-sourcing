package de.vkb.dojo.es.reservations.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerde<T> implements Serde<T>, Serializer<T>, Deserializer<T> {
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    public JsonSerde(ObjectMapper objectMapper, Class<T> clazz) {
        this.objectMapper = objectMapper;
        this.clazz = clazz;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serde.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serde.super.close();
    }

    @Override
    public Serializer<T> serializer() {
        return this;
    }

    @Override
    public Deserializer<T> deserializer() {
        return this;
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null)
            return null;

        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Throwable e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        try {
            return objectMapper.readValue(data, clazz);
        } catch (Throwable e) {
            throw new SerializationException("Error deserializing JSON message", e);
        }
    }
}
