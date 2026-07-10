package com.example.voting.adapters.web.ratelimit;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "voting.rate-limit")
public record RateLimitProperties(long requestsPerMinute) {

    private static final long DEFAULT_REQUESTS_PER_MINUTE = 100;

    public RateLimitProperties {
        if (requestsPerMinute <= 0) {
            requestsPerMinute = DEFAULT_REQUESTS_PER_MINUTE;
        }
    }

    public Duration refillPeriod() {
        return Duration.ofMinutes(1);
    }
}
