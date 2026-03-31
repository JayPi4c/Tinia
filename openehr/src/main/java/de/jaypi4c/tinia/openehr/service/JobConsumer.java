package de.jaypi4c.tinia.openehr.service;

import de.jaypi4c.tinia.common.dto.internal.OpenEhrJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static de.jaypi4c.tinia.common.config.RabbitConfig.OPENEHR_JOBS_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final OpenEhrService openEhrService;


    @RabbitListener(queues = OPENEHR_JOBS_QUEUE)
    public void consume(OpenEhrJob openEhrJob) {
        log.info("Received openehr job content: {}", openEhrJob);
        // TODO: Handle result and send it back to extractor service
        if (openEhrService.process(openEhrJob.tableData(), openEhrJob.date(), openEhrJob.metadata()) != null)
            log.info("Successfully sent data to openEHR server");
        else
            log.error("Error sending data to openEHR server");
    }


}
