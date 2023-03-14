package de.vkb.dojo.es.reservations.kafka;

import de.vkb.dojo.es.reservations.kafka.config.TopicNames;
import de.vkb.dojo.es.reservations.model.command.ReservationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservationCommandProducer {
    @Autowired
    TopicNames topicNames;
    @Autowired
    KafkaTemplate<String, ReservationCommand> template;

    public void publish(ReservationCommand command) {
        template.send(topicNames.getReservationCommand(), command.getOperationId(), command);
    }
}
