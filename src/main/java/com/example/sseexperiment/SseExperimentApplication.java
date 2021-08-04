package com.example.sseexperiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@EnableScheduling
@SpringBootApplication
public class SseExperimentApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SseExperimentApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8089"));
        app.run(args);

    }

}
