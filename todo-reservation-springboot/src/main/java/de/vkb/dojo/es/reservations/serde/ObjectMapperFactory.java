package de.vkb.dojo.es.reservations.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperFactory {
    @Bean
    public ObjectMapper create() {
        var builder = JsonMapper.builder();
        return builder.build();
    }
}
