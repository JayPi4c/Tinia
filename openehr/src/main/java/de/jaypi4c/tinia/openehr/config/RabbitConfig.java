package de.jaypi4c.tinia.openehr.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.jaypi4c.tinia.common.config.RabbitConfig.OPENEHR_JOBS_QUEUE;
import static de.jaypi4c.tinia.common.config.RabbitConfig.OPENEHR_RESULTS_QUEUE;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue openehrJobsQueue() {
        return new Queue(OPENEHR_JOBS_QUEUE, true);
    }

    @Bean
    public Queue openehrResultsQueue() {
        return new Queue(OPENEHR_RESULTS_QUEUE, true);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}