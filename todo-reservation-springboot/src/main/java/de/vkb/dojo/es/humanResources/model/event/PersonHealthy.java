package de.vkb.dojo.es.humanResources.model.event;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-healthy")
public class PersonHealthy extends PersonEvent {
    private final String operationId;
    private final String aggregateId;

    @JsonCreator
    public PersonHealthy(
            @JsonProperty("operatonId") String operationId,
            @JsonProperty("aggregateId") String aggregateId
    ) {
        this.operationId = operationId;
        this.aggregateId = aggregateId;
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
}
