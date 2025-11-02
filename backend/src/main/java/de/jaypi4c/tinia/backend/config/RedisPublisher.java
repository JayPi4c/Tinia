package de.jaypi4c.tinia.backend.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private final StringRedisTemplate template;

    public RedisPublisher(StringRedisTemplate template) {
        this.template = template;
    }

    public void publishProcessingResult(String status) {
        String msg = String.format("{\"status\":\"%s\"}", status);
        template.convertAndSend("processing-results", msg);
    }
}
