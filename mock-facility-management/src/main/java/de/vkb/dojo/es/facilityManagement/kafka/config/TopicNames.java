package de.vkb.dojo.es.facilityManagement.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.kafka.topics")
@Configuration
public class TopicNames {
    private String roomCommand;
    private String roomEventInternal;
    private String roomEventExternal;
    private String roomEventAndState;
    private String roomState;
    private String feedback;

    public String getRoomCommand() {
        return roomCommand;
    }

    public void setRoomCommand(String roomCommand) {
        this.roomCommand = roomCommand;
    }

    public String getRoomEventInternal() {
        return roomEventInternal;
    }

    public void setRoomEventInternal(String roomEventInternal) {
        this.roomEventInternal = roomEventInternal;
    }

    public String getRoomEventExternal() {
        return roomEventExternal;
    }

    public void setRoomEventExternal(String roomEventExternal) {
        this.roomEventExternal = roomEventExternal;
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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
