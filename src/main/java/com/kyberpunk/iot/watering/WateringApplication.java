package com.kyberpunk.iot.watering;

import com.kyberpunk.iot.watering.config.WateringProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(WateringProperties.class)
@SpringBootApplication
public class WateringApplication {
	public static void main(String[] args) {
		SpringApplication.run(WateringApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("TaskThread-");
		executor.setCorePoolSize(100);
		executor.setMaxPoolSize(100);
		executor.initialize();
		return executor;
	}
}
