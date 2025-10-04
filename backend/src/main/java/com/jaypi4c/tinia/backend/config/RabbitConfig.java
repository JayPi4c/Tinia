package com.jaypi4c.tinia.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String JOBS_QUEUE = "ocr-jobs";
    public static final String RESULTS_QUEUE = "ocr-results";

    @Bean
    public Queue jobsQueue() {
        return new Queue(JOBS_QUEUE, true);
    }

    @Bean
    public Queue resultsQueue() {
        return new Queue(RESULTS_QUEUE, true);
    }
}