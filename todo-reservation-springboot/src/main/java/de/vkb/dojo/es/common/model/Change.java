package de.vkb.dojo.es.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Change<T> {
    private final T from;
    private final T to;

    @JsonCreator
    public Change(
            @JsonProperty("from") T from,
            @JsonProperty("to") T to
    ) {
        this.from = from;
        this.to = to;
    }

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }
}
