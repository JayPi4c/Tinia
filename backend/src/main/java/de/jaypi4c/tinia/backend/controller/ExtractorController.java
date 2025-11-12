package de.jaypi4c.tinia.backend.controller;

import de.jaypi4c.tinia.backend.config.RabbitConfig;
import de.jaypi4c.tinia.backend.dto.internal.ExtractorResult;
import de.jaypi4c.tinia.backend.dto.internal.OpenEhrJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExtractorController {


    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.EXTRACTOR_RESULTS_QUEUE)
    public void consume(ExtractorResult extractorResult) {

        if (extractorResult.hasResult()) {
            log.info("Got successful result from extractor");
            rabbitTemplate.convertAndSend(RabbitConfig.OPENEHR_JOBS_QUEUE, new OpenEhrJob(extractorResult.fileId(), extractorResult.page(), extractorResult.result(), extractorResult.date(), extractorResult.metadata()));
        } else log.info("Got unsuccessful result from extractor");

        // TODO: send to validation Service, if validation services are installed (ie reachable by rabbit Mq)


    }

}
