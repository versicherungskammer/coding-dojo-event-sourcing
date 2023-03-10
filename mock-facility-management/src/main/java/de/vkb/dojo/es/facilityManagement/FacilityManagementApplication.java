package de.vkb.dojo.es.facilityManagement;

import de.vkb.dojo.es.facilityManagement.kafka.config.TopicNames;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TopicNames.class)
public class FacilityManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(FacilityManagementApplication.class, args);
	}
}
