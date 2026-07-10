package com.example.voting.adapters.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    private static final String ALL_PATHS = "/**";

    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "OPTIONS");

    private static final List<String> ALLOWED_HEADERS = List.of("*");

    private final List<String> allowedOrigins;

    public CorsConfig(@Value("${voting.cors.allowed-origins}") List<String> allowedOrigins) {
        this.allowedOrigins = List.copyOf(allowedOrigins);
    }

    @Bean
    public CorsFilter corsFilter() {
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ALL_PATHS, corsConfiguration());

        return new CorsFilter(source);
    }

    private CorsConfiguration corsConfiguration() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);

        return configuration;
    }
}
