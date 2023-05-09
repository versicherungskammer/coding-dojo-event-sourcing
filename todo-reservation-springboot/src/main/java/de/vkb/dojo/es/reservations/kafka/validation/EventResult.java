package de.vkb.dojo.es.reservations.kafka.validation;

import de.vkb.dojo.es.common.model.feedback.Feedback;
import de.vkb.dojo.es.reservations.model.event.ReservationEvent;
import de.vkb.dojo.es.reservations.model.state.Reservation;

public class EventResult {

    private Feedback feedback;

    private ReservationEvent event;

    private Reservation reservation;
    private String operationId;

    public EventResult(Feedback feedback, String operationId) {
        this.feedback = feedback;
        this.event = null;
        this.reservation = null;
        this.operationId = operationId;
    }

    public EventResult(ReservationEvent event, Reservation reservation, Feedback feedback){
        this.feedback = feedback;
        this.event = event;
        this.reservation = reservation;
        this.operationId = null;
    }


    public Feedback getFeedback() {
        return feedback;
    }

    public ReservationEvent getEvent() {
        return event;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public String getOperationId() { return operationId; }
}
