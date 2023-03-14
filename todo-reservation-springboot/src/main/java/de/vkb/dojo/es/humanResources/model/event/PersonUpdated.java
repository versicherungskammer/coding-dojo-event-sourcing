package de.vkb.dojo.es.humanResources.model.event;

import com.fasterxml.jackson.annotation.*;
import de.vkb.dojo.es.common.model.Change;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-updated")
public class PersonUpdated extends PersonEvent {
    private final String operationId;
    private final String aggregateId;

    private final Change<String> username;
    private final Change<String> fullname;

    @JsonCreator
    public PersonUpdated(
            @JsonProperty("operatonId") String operationId,
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("username") Change<String> username,
            @JsonProperty("fullname") Change<String> fullname
    ) {
        this.operationId = operationId;
        this.aggregateId = aggregateId;
        this.username = username;
        this.fullname = fullname;
    }

    @Override
    @JsonGetter
    public String getOperationId() {
        return operationId;
    }

    @Override
    @JsonGetter
    public String getAggregateId() {
        return aggregateId;
    }

    @JsonGetter
    public Change<String> getUsername() {
        return username;
    }

    @JsonGetter
    public Change<String> getFullname() {
        return fullname;
    }
}
