package com.weather.app.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitService {

        private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

        public Bucket resolveBucket(String apiKey) {
            return cache.computeIfAbsent(apiKey, this::newBucket);
        }

        private Bucket newBucket(String apiKey) {
            log.info("RateLimitService:newBucket: Setting Rate Limit - 5 requests per hour for the api key ");
            Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofHours(1)));
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        }
    }
