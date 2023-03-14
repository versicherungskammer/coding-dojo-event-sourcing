package de.vkb.dojo.es.humanResources.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    private final String username;
    private final String fullname;
    private final Boolean sick;

    @JsonCreator
    public Person(
            @JsonProperty("username") String username,
            @JsonProperty("fullname") String fullname,
            @JsonProperty("sick") Boolean sick
    ) {
        this.username = username;
        this.fullname = fullname;
        this.sick = sick;
    }

    @JsonGetter
    public String getUsername() {
        return username;
    }
    @JsonGetter
    public String getFullname() {
        return fullname;
    }
    @JsonGetter
    public Boolean getSick() {
        return sick;
    }
}
