package com.kodsonApp.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String KODSON_LLC = "Kodson, LLC";
    public static final String RESTAURANT_ADMINISTRATION = "Kodson Erp";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";

    // Properly secured public URLs - only authentication and health endpoints
    public static final String[] PUBLIC_URLS = {
        "/api2/auth/login",
        "/api2/auth/register",
        "/api2/user/login", // <-- Added to allow public access
        "/actuator/health",
        "/actuator/info",
        "/api2/public/**",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    };

    // Admin only URLs
    public static final String[] ADMIN_URLS = {
        "/api2/admin/**",
        "/actuator/**"
    };

    // Manager and above URLs
    public static final String[] MANAGER_URLS = {
        "/api2/reports/**",
        "/api2/analytics/**"
    };
}
