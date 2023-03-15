package de.vkb.dojo.es.reservations.kafka.validation;

import de.vkb.dojo.es.common.model.feedback.Feedback;
import de.vkb.dojo.es.reservations.model.event.ReservationEvent;

public class CommandResult {

    public CommandResult(Feedback feedback, ReservationEvent event, String operationId){
        this.feedback = feedback;
        this.event = event;
        this.operationId = operationId;
    }

    private String operationId;
    private Feedback feedback;
    private ReservationEvent event;

    public Feedback getFeedback() {
        return feedback;
    }

    public ReservationEvent getEvent(){
        return event;
    }

    public String getOperationId(){
        return operationId;
    }
}
