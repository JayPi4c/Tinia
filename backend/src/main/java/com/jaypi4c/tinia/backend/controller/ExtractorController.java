package com.jaypi4c.tinia.backend.controller;

import com.jaypi4c.tinia.backend.config.RabbitConfig;
import com.jaypi4c.tinia.backend.dto.internal.ExtractorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExtractorController {

    @RabbitListener(queues = RabbitConfig.EXTRACTOR_RESULTS_QUEUE)
    public void consume(ExtractorResult extractorResult) {

        if (extractorResult.hasResult()) {
            log.info("Got successful result from extractor");
        } else log.info("Got unsuccessful result from extractor");

        // TODO: send to validation Service, if validation services are installed (ie reachable by rabbit Mq)


    }

}
