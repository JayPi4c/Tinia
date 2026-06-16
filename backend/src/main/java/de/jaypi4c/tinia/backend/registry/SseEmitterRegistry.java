package de.jaypi4c.tinia.backend.registry;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitterRegistry {
    // local emitter map as network connection elements (emitter) cant be shared between instances.
    // jobId / client emitter
    private final Map<UUID, SseEmitter> clients = new ConcurrentHashMap<>();

    public UUID register() {
        UUID jobId = UUID.randomUUID();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        clients.put(jobId, emitter);

        Runnable cleanup = () -> clients.remove(jobId);

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(_ -> cleanup.run());

        return jobId;
    }

    public Optional<SseEmitter> get(UUID jobId) {
        return Optional.ofNullable(clients.get(jobId));
    }

    public void remove(UUID jobId) {
        clients.remove(jobId);
    }

    public Map<UUID, SseEmitter> getAll() {
        return clients;
    }

}