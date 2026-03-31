package de.jaypi4c.tinia.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.jaypi4c.tinia.common.config.RabbitConfig.*;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue extractorJobsQueue() {
        return new Queue(EXTRACTOR_JOBS_QUEUE, true);
    }

    @Bean
    public Queue extractorResultsQueue() {
        return new Queue(EXTRACTOR_RESULTS_QUEUE, true);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
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