package com.kyberpunk.iot.watering;

import com.kyberpunk.iot.watering.config.WateringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WateringConfiguration.class)
public class WateringApplication {

	public static void main(String[] args) {
		SpringApplication.run(WateringApplication.class, args);
	}

}
