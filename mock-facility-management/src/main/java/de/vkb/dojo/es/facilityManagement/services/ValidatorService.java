package de.vkb.dojo.es.facilityManagement.services;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidatorService {
    public boolean isValidName(Optional<String> name) {
        return name.map( it -> !it.isBlank() ).orElse(false);
    }
    public boolean isValidName(String name) {
        return isValidName(Optional.ofNullable(name));
    }
}
