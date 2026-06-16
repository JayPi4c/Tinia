package de.jaypi4c.tinia.backend.controller.internal;

import de.jaypi4c.tinia.backend.registry.SseEmitterRegistry;
import de.jaypi4c.tinia.common.dto.internal.DetectorResult;
import de.jaypi4c.tinia.common.dto.internal.model.BoundingBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

import static de.jaypi4c.tinia.common.config.RabbitConfig.DETECTOR_RESULTS_QUEUE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DetectorController {

    private final SseEmitterRegistry sseEmitterRegistry;


    @RabbitListener(queues = DETECTOR_RESULTS_QUEUE)
    public void consume(DetectorResult detectorResult) {

        if (detectorResult.hasContent()) {
            log.info("Got successful result from detector. Show in Frontend");

            sseEmitterRegistry.get(detectorResult.jobId()).ifPresentOrElse(emitter -> {

                try {
                    EventDto event = buildResponse(detectorResult.jobId(), detectorResult.page(), detectorResult.table());
                    emitter.send(SseEmitter.event().data(event));
                    log.info("Successfully send event via sse");
                } catch (IOException e) {
                    log.error("failed", e);
                    emitter.complete();
                }
            }, () -> log.info("Did not found emitter for job {}", detectorResult.jobId()));


            // FIXME inform frontend using sse

            //  rabbitTemplate.convertAndSend(EXTRACTOR_JOBS_QUEUE, new ExtractorJob(detectorResult.jobId(), detectorResult.page(), detectorResult.date(), detectorResult.table(), detectorResult.documentBytes()));
        } else log.info("Got unsuccessful result from detector");

        // TODO: send to validation Service, if validation services are installed (ie reachable by rabbit Mq)


    }

    private EventDto buildResponse(UUID jobId, int page, BoundingBox[][] cells) {
        String imageUrl = "/api/v1/upload/jobs/" + jobId + "/pages/" + page + "/image";
        return new EventDto("TABLE_DETECTED", jobId, page, imageUrl, cells);
    }

    record EventDto(String type, UUID jobId, int page, String imageUrl, BoundingBox[][] cells) {
    }

}
