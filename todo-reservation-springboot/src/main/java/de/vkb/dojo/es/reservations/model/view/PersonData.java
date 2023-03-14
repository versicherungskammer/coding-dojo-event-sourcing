package de.vkb.dojo.es.reservations.model.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.humanResources.model.state.Person;

public class PersonData extends Person {
    private final String id;

    @JsonCreator
    public PersonData(
            @JsonProperty("id") String id,
            Person template
    ) {
        super(template.getUsername(), template.getFullname(), template.getSick());
        this.id = id;
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }
}
