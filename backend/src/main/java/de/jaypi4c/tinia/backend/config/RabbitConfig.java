package de.jaypi4c.tinia.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXTRACTOR_JOBS_QUEUE = "extractor-jobs";
    public static final String EXTRACTOR_RESULTS_QUEUE = "extractor-results";
    public static final String OPENEHR_JOBS_QUEUE = "openehr-jobs";
    public static final String OPENEHR_RESULTS_QUEUE = "openehr-results";

    @Bean
    public Queue extractorJobsQueue() {
        return new Queue(EXTRACTOR_JOBS_QUEUE, true);
    }

    @Bean
    public Queue extractorResultsQueue() {
        return new Queue(EXTRACTOR_RESULTS_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue openehrJobsQueue() {
        return new Queue(OPENEHR_JOBS_QUEUE, true);
    }

    @Bean
    public Queue openehrResultsQueue() {
        return new Queue(OPENEHR_RESULTS_QUEUE, true);
    }

}