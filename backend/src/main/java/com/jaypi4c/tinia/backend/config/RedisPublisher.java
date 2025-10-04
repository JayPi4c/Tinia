package com.jaypi4c.tinia.backend.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private final StringRedisTemplate template;

    public RedisPublisher(StringRedisTemplate template) {
        this.template = template;
    }

    public void publishBookUpdate(String bookName, String status) {
        String msg = String.format("{\"bookId\":\"%s\",\"status\":\"%s\"}", bookName, status);
        template.convertAndSend("book-updates", msg);
    }
}