package com.kodsonApp.filter;

import com.kodsonApp.constant.SecurityConstant;
import com.kodsonApp.utility.JWTTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final JWTTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Handle preflight requests
        if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            logger.info("Processing request to: {} with token present: {}", request.getRequestURI(), token != null);

            if (token != null && !jwtTokenProvider.isTokenBlacklisted(token)) {
                String username = jwtTokenProvider.getSubject(token);
                logger.info("Extracted username from token: {}", username);

                if (StringUtils.hasText(username) &&
                    jwtTokenProvider.isTokenValid(username, token) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                    List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                    logger.info("Extracted authorities from token: {}", authorities);

                    Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    logger.info("Successfully authenticated user: {} with authorities: {}", username, authorities);
                } else {
                    logger.warn("Token validation failed for user: {} or user already authenticated", username);
                }
            } else {
                logger.warn("No valid token found in request to: {}", request.getRequestURI());
            }
        } catch (Exception e) {
            logger.error("JWT authentication failed: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // First try standard Authorization header
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) &&
            authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            return authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
        }

        // Then try custom Jwt-Token header
        String jwtTokenHeader = request.getHeader(SecurityConstant.JWT_TOKEN_HEADER);
        if (StringUtils.hasText(jwtTokenHeader) &&
            jwtTokenHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            return jwtTokenHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
        }

        // Also try Jwt-Token header without Bearer prefix (some clients send just the token)
        if (StringUtils.hasText(jwtTokenHeader)) {
            return jwtTokenHeader;
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // Skip JWT filtering for public endpoints
        for (String publicUrl : SecurityConstant.PUBLIC_URLS) {
            if (path.matches(publicUrl.replace("**", ".*"))) {
                return true;
            }
        }

        return false;
    }
}
