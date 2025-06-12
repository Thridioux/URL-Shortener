package com.erdem.urlshortener.service.impl;

import com.erdem.urlshortener.dto.CreateShortUrlRequest;
import com.erdem.urlshortener.dto.ShortUrlResponse;
import com.erdem.urlshortener.entity.ShortUrl;
import com.erdem.urlshortener.exception.NotFoundException;
import com.erdem.urlshortener.repository.ShortUrlRepository;
import com.erdem.urlshortener.service.UrlShorteningService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.erdem.urlshortener.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlShorteningServiceImpl implements UrlShorteningService {

    private static final Logger logger = LoggerFactory.getLogger(UrlShorteningServiceImpl.class);
    private final ShortUrlRepository repository;
    private final StringRedisTemplate redisTemplate;
    private final AnalyticsService analyticsService;

    @Value("${app.domain:http://localhost:8080}")
    private String appDomain;

    public UrlShorteningServiceImpl(ShortUrlRepository repository,
                                    StringRedisTemplate redisTemplate,
                                    AnalyticsService analyticsService) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.analyticsService = analyticsService;
    }

    @Override
    @Transactional
    public ShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        String uuid = UUID.randomUUID().toString();
        String shortPath = uuid.substring(0, 8);
        String shortUrl = appDomain + "/api/" + shortPath;

        ShortUrl entity = new ShortUrl(uuid, shortUrl, request.getLongUrl(), LocalDateTime.now());
        repository.save(entity);

        String cacheKey = "urlMap:" + shortPath;
        try {
            redisTemplate.opsForValue().set(cacheKey, request.getLongUrl(), Duration.ofDays(1));
        } catch (Exception e) {
            logger.warn("Redis unavailable, skipping cache set for {}", shortPath, e);
        }

        return new ShortUrlResponse(entity.getShortUrl(), entity.getUuid());
    }

    @Override
    @Transactional
    public String getLongUrl(String shortPath) {
        String cacheKey = "urlMap:" + shortPath;
        String cached = null;
        try {
            cached = redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception e) {
            logger.warn("Redis unavailable, skipping cache read for {}", shortPath, e);
        }
        if (cached != null) {
            analyticsService.recordHit(shortPath);
            return cached;
        }

        ShortUrl entity = repository.findByShortUrl(appDomain + "/api/" + shortPath)
                .orElseThrow(() -> new NotFoundException("Short URL not found"));
        entity.setUsedCount(entity.getUsedCount() + 1);
        repository.save(entity);

        try {
            redisTemplate.opsForValue().set(cacheKey, entity.getLongUrl(), Duration.ofDays(1));
        } catch (Exception e) {
            logger.warn("Redis unavailable, skipping cache set for {}", shortPath, e);
        }
        analyticsService.recordHit(shortPath);
        return entity.getLongUrl();
    }
}
