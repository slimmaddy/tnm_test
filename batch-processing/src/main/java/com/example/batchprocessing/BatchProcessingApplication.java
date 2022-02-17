package com.example.batchprocessing;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
@EnableRabbit
public class BatchProcessingApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BatchProcessingApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);

	}

}
