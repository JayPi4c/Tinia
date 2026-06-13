package de.jaypi4c.tinia.detector.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.jaypi4c.tinia.common.config.RabbitConfig.DETECTOR_JOBS_QUEUE;
import static de.jaypi4c.tinia.common.config.RabbitConfig.DETECTOR_RESULTS_QUEUE;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue detectorJobsQueue() {
        return new Queue(DETECTOR_JOBS_QUEUE, true);
    }

    @Bean
    public Queue detectorResultsQueue() {
        return new Queue(DETECTOR_RESULTS_QUEUE, true);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}