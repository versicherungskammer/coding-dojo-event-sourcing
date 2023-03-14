package de.vkb.dojo.es.reservations.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.kafka.topics")
@Configuration
public class TopicNames {
    private String personEvent;
    private String personEventAndState;
    private String personState;
    private String roomEvent;
    private String roomEventAndState;
    private String roomState;
    private String reservationCommand;
    private String reservationEventInternal;
    private String reservationEventExternal;
    private String reservationEventAndState;
    private String reservationState;
    private String feedback;

    public String getReservationCommand() {
        return reservationCommand;
    }

    public void setReservationCommand(String reservationCommand) {
        this.reservationCommand = reservationCommand;
    }

    public String getReservationEventInternal() {
        return reservationEventInternal;
    }

    public void setReservationEventInternal(String reservationEventInternal) {
        this.reservationEventInternal = reservationEventInternal;
    }

    public String getReservationEventExternal() {
        return reservationEventExternal;
    }

    public void setReservationEventExternal(String reservationEventExternal) {
        this.reservationEventExternal = reservationEventExternal;
    }

    public String getReservationEventAndState() {
        return reservationEventAndState;
    }

    public void setReservationEventAndState(String reservationEventAndState) {
        this.reservationEventAndState = reservationEventAndState;
    }

    public String getReservationState() {
        return reservationState;
    }

    public void setReservationState(String reservationState) {
        this.reservationState = reservationState;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPersonEvent() {
        return personEvent;
    }

    public void setPersonEvent(String personEvent) {
        this.personEvent = personEvent;
    }

    public String getPersonEventAndState() {
        return personEventAndState;
    }

    public void setPersonEventAndState(String personEventAndState) {
        this.personEventAndState = personEventAndState;
    }

    public String getPersonState() {
        return personState;
    }

    public void setPersonState(String personState) {
        this.personState = personState;
    }

    public String getRoomEvent() {
        return roomEvent;
    }

    public void setRoomEvent(String roomEvent) {
        this.roomEvent = roomEvent;
    }

    public String getRoomEventAndState() {
        return roomEventAndState;
    }

    public void setRoomEventAndState(String roomEventAndState) {
        this.roomEventAndState = roomEventAndState;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }
}
