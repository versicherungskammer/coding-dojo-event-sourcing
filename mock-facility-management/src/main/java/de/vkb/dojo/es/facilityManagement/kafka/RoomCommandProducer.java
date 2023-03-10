package de.vkb.dojo.es.facilityManagement.kafka;

import de.vkb.dojo.es.facilityManagement.kafka.config.TopicNames;
import de.vkb.dojo.es.facilityManagement.model.command.RoomCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RoomCommandProducer {
    @Autowired
    TopicNames topicNames;
    @Autowired
    KafkaTemplate<String, RoomCommand> template;

    public void publish(RoomCommand command) {
        template.send(topicNames.getRoomCommand(), command.getOperationId(), command);
    }
}
