package de.vkb.dojo.es.facilityManagement.services.events;


import de.vkb.dojo.es.facilityManagement.model.event.Event;
import de.vkb.dojo.es.facilityManagement.model.feedback.Feedback;
import de.vkb.dojo.es.facilityManagement.model.feedback.SuccessFeedback;

public class EventAggregatorResult<E extends Event, A> {
    private final E event;
    private final A aggregate;
    private final Feedback feedback;

    public EventAggregatorResult(E event, A aggregate) {
        this.event = event;
        this.aggregate = aggregate;
        this.feedback = new SuccessFeedback(event.getReference());
    }

    public EventAggregatorResult(E event, Feedback feedback) {
        this.event = event;
        this.aggregate = null;
        this.feedback = feedback;
    }

    public EventAggregatorResult(E event) {
        this.event = event;
        this.aggregate = null;
        this.feedback = new SuccessFeedback(event.getReference());
    }

    public E getEvent() {
        return event;
    }

    public A getAggregate() {
        return aggregate;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}
