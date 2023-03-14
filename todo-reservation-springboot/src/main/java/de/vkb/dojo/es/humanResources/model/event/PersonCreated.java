package de.vkb.dojo.es.humanResources.model.event;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-created")
public class PersonCreated extends PersonEvent {
    private final String operationId;
    private final String aggregateId;

    private final String username;
    private final String fullname;

    @JsonCreator
    public PersonCreated(
            @JsonProperty("operationId") String operationId,
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("username") String username,
            @JsonProperty("fullname") String fullname
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
    public String getUsername() {
        return username;
    }

    @JsonGetter
    public String getFullname() {
        return fullname;
    }
}
