package com.jaypi4c.tinia.openehr.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String OPENEHR_JOBS_QUEUE = "openehr-jobs";
    public static final String OPENEHR_RESULTS_QUEUE = "openehr-results";

    @Bean
    public Queue openehrJobsQueue() {
        return new Queue(OPENEHR_JOBS_QUEUE, true);
    }

    @Bean
    public Queue openehrResultsQueue() {
        return new Queue(OPENEHR_RESULTS_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}