package com.example.voting.adapters.web.ratelimit;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RateLimitFilter extends HttpFilter {

    private static final String RATE_LIMITED_PATH_PREFIX = "/api";

    private static final String FORWARDED_FOR_HEADER = "X-Forwarded-For";

    private static final String FORWARDED_FOR_SEPARATOR = ",";

    private static final String RATE_LIMIT_EXCEEDED_BODY =
            "{\"type\":\"about:blank\",\"title\":\"Too Many Requests\",\"status\":429,"
                    + "\"detail\":\"Rate limit exceeded\",\"errorCode\":\"RATE_LIMIT_EXCEEDED\"}";

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isRateLimited(request) && !rateLimitService.tryConsume(resolveClientIdentifier(request))) {
            rejectWithTooManyRequests(response);

            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isRateLimited(HttpServletRequest request) {
        return request.getRequestURI().startsWith(RATE_LIMITED_PATH_PREFIX);
    }

    private String resolveClientIdentifier(HttpServletRequest request) {
        var forwardedFor = request.getHeader(FORWARDED_FOR_HEADER);

        if (StringUtils.hasText(forwardedFor)) {
            return forwardedFor.split(FORWARDED_FOR_SEPARATOR)[0].strip();
        }

        return request.getRemoteAddr();
    }

    private void rejectWithTooManyRequests(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.getWriter().write(RATE_LIMIT_EXCEEDED_BODY);
    }
}
