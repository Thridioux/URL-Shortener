package com.erdem.urlshortener.service.impl;

import com.erdem.urlshortener.service.AnalyticsService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final StringRedisTemplate redisTemplate;

    public AnalyticsServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Async
    public void recordHit(String shortPath) {
        String key = "hits:" + shortPath;
        redisTemplate.opsForList().rightPush(key, Instant.now().toString());
    }
}
