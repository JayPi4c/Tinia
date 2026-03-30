package de.jaypi4c.tinia.openehr.service;

import de.jaypi4c.tinia.openehr.OpenEhrManager;
import de.jaypi4c.tinia.openehr.config.RabbitConfig;
import de.jaypi4c.tinia.openehr.dto.internal.OpenEhrJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final OpenEhrManager openEhrManager;

    @RabbitListener(queues = RabbitConfig.OPENEHR_JOBS_QUEUE)
    public void consume(OpenEhrJob openEhrJob) {
        log.info("Received openehr job content: {}", openEhrJob);
        if (openEhrManager.sendNephroMedikationData(openEhrManager.createComposition(openEhrJob.tableData(), openEhrJob.date(), openEhrJob.metadata())))
            log.info("Successfully sent data to openEHR server");
        else
            log.error("Error sending data to openEHR server");
    }

    @PostConstruct
    public void init() {
        openEhrManager.checkForTemplate();
    }

}
