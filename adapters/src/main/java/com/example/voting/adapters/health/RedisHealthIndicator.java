package com.example.voting.adapters.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component("redisHealthIndicator")
@RequiredArgsConstructor
public class RedisHealthIndicator implements HealthIndicator {

    private static final String PONG = "PONG";

    private static final String RESPONSE_DETAIL = "response";

    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public Health health() {
        try (var connection = redisConnectionFactory.getConnection()) {
            var response = connection.ping();

            return Health.up().withDetail(RESPONSE_DETAIL, response == null ? PONG : response).build();
        } catch (RuntimeException exception) {
            return Health.down(exception).build();
        }
    }
}
