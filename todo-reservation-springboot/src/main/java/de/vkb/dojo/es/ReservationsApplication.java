package de.vkb.dojo.es;

import de.vkb.dojo.es.reservations.kafka.config.TopicNames;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TopicNames.class)
public class ReservationsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReservationsApplication.class, args);
	}
}
