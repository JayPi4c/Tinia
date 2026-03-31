package de.jaypi4c.tinia.extractor.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.jaypi4c.tinia.common.config.RabbitConfig.EXTRACTOR_JOBS_QUEUE;
import static de.jaypi4c.tinia.common.config.RabbitConfig.EXTRACTOR_RESULTS_QUEUE;

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
}