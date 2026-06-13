package de.jaypi4c.tinia.backend.controller;

import de.jaypi4c.tinia.common.dto.internal.ExtractorResult;
import de.jaypi4c.tinia.common.dto.internal.OpenEhrJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;

import static de.jaypi4c.tinia.common.config.RabbitConfig.EXTRACTOR_RESULTS_QUEUE;
import static de.jaypi4c.tinia.common.config.RabbitConfig.OPENEHR_JOBS_QUEUE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExtractorController {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = EXTRACTOR_RESULTS_QUEUE)
    public void consume(ExtractorResult extractorResult) {

        if (extractorResult.hasResult()) {
            log.info("Got successful result from extractor. Forwarding to OpenEHR");
            rabbitTemplate.convertAndSend(OPENEHR_JOBS_QUEUE, new OpenEhrJob(extractorResult.jobId(), extractorResult.page(), extractorResult.result(), extractorResult.date(), extractorResult.metadata()));
        } else log.info("Got unsuccessful result from extractor");

        // TODO: send to validation Service, if validation services are installed (ie reachable by rabbit Mq)


    }

}
