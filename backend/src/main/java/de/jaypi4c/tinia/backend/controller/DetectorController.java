package de.jaypi4c.tinia.backend.controller;

import de.jaypi4c.tinia.common.dto.internal.DetectorResult;
import de.jaypi4c.tinia.common.dto.internal.ExtractorJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;

import static de.jaypi4c.tinia.common.config.RabbitConfig.DETECTOR_RESULTS_QUEUE;
import static de.jaypi4c.tinia.common.config.RabbitConfig.EXTRACTOR_JOBS_QUEUE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DetectorController {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DETECTOR_RESULTS_QUEUE)
    public void consume(DetectorResult detectorResult) {

        if (detectorResult.hasContent()) {
            log.info("Got successful result from detector. Forwarding to Extractor");
            rabbitTemplate.convertAndSend(EXTRACTOR_JOBS_QUEUE, new ExtractorJob(detectorResult.jobId(), detectorResult.page(), detectorResult.date(), detectorResult.table(), detectorResult.documentBytes()));
        } else log.info("Got unsuccessful result from detector");

        // TODO: send to validation Service, if validation services are installed (ie reachable by rabbit Mq)


    }

}
