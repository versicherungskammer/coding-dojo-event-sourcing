package de.vkb.dojo.es.humanResources.model.eventState;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.humanResources.model.event.PersonEvent;
import de.vkb.dojo.es.humanResources.model.state.Person;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonEventAndState {
    private final PersonEvent event;
    private final Person state;

    @JsonCreator
    public PersonEventAndState(
            @JsonProperty("event") PersonEvent event,
            @JsonProperty("state") Person state
    ) {
        this.event = event;
        this.state = state;
    }

    @JsonGetter
    public PersonEvent getEvent() {
        return event;
    }

    @JsonGetter
    public Person getState() {
        return state;
    }
}
