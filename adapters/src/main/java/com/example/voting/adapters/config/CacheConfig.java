package com.example.voting.adapters.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String ELECTIONS_CACHE = "elections";

    private static final Duration ELECTION_CACHE_TIME_TO_LIVE = Duration.ofMinutes(10);

    @Bean
    public RedisCacheManagerBuilderCustomizer electionsCacheCustomizer() {
        return builder -> builder.withCacheConfiguration(ELECTIONS_CACHE, electionsCacheConfiguration());
    }

    private RedisCacheConfiguration electionsCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ELECTION_CACHE_TIME_TO_LIVE)
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
