package de.vkb.dojo.es.facilityManagement.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonSerdeFactory {
    private final ObjectMapper objectMapper;

    @Autowired
    public JsonSerdeFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> JsonSerde<T> of(Class<T> clazz) {
        return new JsonSerde<>(objectMapper, clazz);
    }
}
