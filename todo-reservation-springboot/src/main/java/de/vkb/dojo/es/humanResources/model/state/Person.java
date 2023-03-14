package de.vkb.dojo.es.humanResources.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    private final String username;
    private final String fullname;

    @JsonCreator
    public Person(
            @JsonProperty("username") String username,
            @JsonProperty("fullname") String fullname
    ) {
        this.username = username;
        this.fullname = fullname;
    }

    @JsonGetter
    public String getUsername() {
        return username;
    }
    @JsonGetter
    public String getFullname() {
        return fullname;
    }
}
