package com.jaypi4c.tinia.openehr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobConsumer {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "openehr-jobs")
    public void receiveOpenehrJob(String content) {
        log.info("Received openehr job content: {}", content);
        // TODO process the content and interact with openEHR system


        rabbitTemplate.convertAndSend("openehr-results", content);
    }

}
