package com.example.voting.adapters.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.example.voting.adapters.web.ratelimit.RateLimitFilter;
import com.example.voting.adapters.web.ratelimit.RateLimitProperties;
import com.example.voting.adapters.web.ratelimit.RateLimitService;
import com.example.voting.adapters.web.security.SecurityHeadersFilter;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class WebConfig {

    private static final String ALL_PATHS = "/*";

    private static final int SECURITY_HEADERS_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

    private static final int RATE_LIMIT_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 20;

    @Bean
    public FilterRegistrationBean<SecurityHeadersFilter> securityHeadersFilterRegistration() {
        var registration = new FilterRegistrationBean<>(new SecurityHeadersFilter());
        registration.addUrlPatterns(ALL_PATHS);
        registration.setOrder(SECURITY_HEADERS_FILTER_ORDER);

        return registration;
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration(RateLimitService rateLimitService) {
        var registration = new FilterRegistrationBean<>(new RateLimitFilter(rateLimitService));
        registration.addUrlPatterns(ALL_PATHS);
        registration.setOrder(RATE_LIMIT_FILTER_ORDER);

        return registration;
    }
}
