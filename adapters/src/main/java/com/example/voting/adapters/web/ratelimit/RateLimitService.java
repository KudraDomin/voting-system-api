package com.example.voting.adapters.web.ratelimit;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitService {

    private static final long SINGLE_TOKEN = 1;

    private final ConcurrentMap<String, Bucket> bucketsByClient = new ConcurrentHashMap<>();

    private final long requestsPerMinute;

    private final Duration refillPeriod;

    public RateLimitService(RateLimitProperties rateLimitProperties) {
        this.requestsPerMinute = rateLimitProperties.requestsPerMinute();
        this.refillPeriod = rateLimitProperties.refillPeriod();
    }

    public boolean tryConsume(String clientIdentifier) {
        return resolveBucket(clientIdentifier).tryConsume(SINGLE_TOKEN);
    }

    private Bucket resolveBucket(String clientIdentifier) {
        return bucketsByClient.computeIfAbsent(clientIdentifier, ignored -> newBucket());
    }

    private Bucket newBucket() {
        var limit = Bandwidth.builder()
                .capacity(requestsPerMinute)
                .refillGreedy(requestsPerMinute, refillPeriod)
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
