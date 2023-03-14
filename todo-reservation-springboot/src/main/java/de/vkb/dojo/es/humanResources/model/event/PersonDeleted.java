package de.vkb.dojo.es.humanResources.model.event;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("person-deleted")
public class PersonDeleted extends PersonEvent {
    private final String operationId;
    private final String aggregateId;

    @JsonCreator
    public PersonDeleted(
            @JsonProperty("operationId") String operationId,
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
