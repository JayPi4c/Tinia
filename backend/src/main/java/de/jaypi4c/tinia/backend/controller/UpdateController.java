package de.jaypi4c.tinia.backend.controller;

import de.jaypi4c.tinia.backend.registry.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UpdateController {

    private final SseEmitterRegistry sseEmitterRegistry;

    @GetMapping(value = "/api/v1/upload/jobs/{jobId}/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamUpdates(@PathVariable UUID jobId) throws IOException {
        Optional<SseEmitter> emitterOpt = sseEmitterRegistry.get(jobId);
        if (emitterOpt.isPresent()) {
            SseEmitter emitter = emitterOpt.get();
            emitter.send(SseEmitter.event().comment("connected")); // send initial connected event for the headers to be sent
            return emitter;
        }
        throw new IOException("Failed to obtain emitter");
    }


}
