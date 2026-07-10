package com.example.voting.adapters.web.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SecurityHeadersFilter extends HttpFilter {

    private static final String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";

    private static final String CONTENT_SECURITY_POLICY_VALUE = "default-src 'self'";

    private static final String CONTENT_TYPE_OPTIONS_HEADER = "X-Content-Type-Options";

    private static final String CONTENT_TYPE_OPTIONS_VALUE = "nosniff";

    private static final String FRAME_OPTIONS_HEADER = "X-Frame-Options";

    private static final String FRAME_OPTIONS_VALUE = "DENY";

    private static final String STRICT_TRANSPORT_SECURITY_HEADER = "Strict-Transport-Security";

    private static final String STRICT_TRANSPORT_SECURITY_VALUE = "max-age=31536000; includeSubDomains";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        applySecurityHeaders(response);

        chain.doFilter(request, response);
    }

    private void applySecurityHeaders(HttpServletResponse response) {
        response.setHeader(CONTENT_SECURITY_POLICY_HEADER, CONTENT_SECURITY_POLICY_VALUE);
        response.setHeader(CONTENT_TYPE_OPTIONS_HEADER, CONTENT_TYPE_OPTIONS_VALUE);
        response.setHeader(FRAME_OPTIONS_HEADER, FRAME_OPTIONS_VALUE);
        response.setHeader(STRICT_TRANSPORT_SECURITY_HEADER, STRICT_TRANSPORT_SECURITY_VALUE);
    }
}
