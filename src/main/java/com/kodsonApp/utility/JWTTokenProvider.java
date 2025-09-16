package com.kodsonApp.utility;

import com.kodsonApp.constant.SecurityConstant;
import com.kodsonApp.domain.Kodson;
import com.kodsonApp.domain.KodsonPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JWTTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateJwtToken(KodsonPrincipal kodsonPrincipal) {
        try {
            // Create header
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");

            // Create payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("iss", SecurityConstant.KODSON_LLC);
            payload.put("aud", SecurityConstant.RESTAURANT_ADMINISTRATION);
            payload.put("sub", kodsonPrincipal.getUsername());
            payload.put("iat", System.currentTimeMillis() / 1000);
            payload.put("exp", (System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME) / 1000);

            // Get user from principal to access role
            KodsonPrincipal principal = (KodsonPrincipal) kodsonPrincipal;
            Kodson user = principal.getKodson();

            // Extract role from user (remove ROLE_ prefix if present for Spring Security compatibility)
            String role = user.getRole();
            if (role != null && role.startsWith("ROLE_")) {
                role = role.substring(5); // Remove "ROLE_" prefix
            }
            payload.put("role", role);

            // Get authorities from principal
            List<String> authorities = kodsonPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Add role as authority for Spring Security role checking
            if (role != null) {
                authorities.add("ROLE_" + role);
            }

            payload.put(SecurityConstant.AUTHORITIES, authorities);
            payload.put("userId", kodsonPrincipal.getUsername());

            // Create token
            String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(objectMapper.writeValueAsString(header).getBytes(StandardCharsets.UTF_8));
            String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(objectMapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8));

            String data = encodedHeader + "." + encodedPayload;
            String signature = hmacSha256(data, secret);

            return data + "." + signature;
        } catch (Exception e) {
            logger.error("Error generating JWT token: {}", e.getMessage());
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claims.get(SecurityConstant.AUTHORITIES);

            if (authorities == null) {
                return List.of();
            }

            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error extracting authorities from token: {}", e.getMessage());
            return List.of();
        }
    }

    private Map<String, Object> getClaimsFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid JWT token format");
            }

            // Verify signature
            String data = parts[0] + "." + parts[1];
            String expectedSignature = hmacSha256(data, secret);
            if (!expectedSignature.equals(parts[2])) {
                throw new RuntimeException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
            }

            // Decode payload
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);

            @SuppressWarnings("unchecked")
            Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);

            return claims;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw new RuntimeException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
    }

    public boolean isTokenValid(String username, String token) {
        try {
            return StringUtils.isNotEmpty(username) && !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            String username = getSubject(token);
            return isTokenValid(username, token);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            Object exp = claims.get("exp");
            if (exp instanceof Integer) {
                long expiration = ((Integer) exp).longValue() * 1000; // Convert to milliseconds
                return expiration < System.currentTimeMillis();
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public String getSubject(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            return (String) claims.get("sub");
        } catch (Exception e) {
            logger.error("Error extracting subject from token: {}", e.getMessage());
            return null;
        }
    }

    public String getUserId(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            return (String) claims.get("userId");
        } catch (Exception e) {
            logger.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }

    public boolean isTokenBlacklisted(String token) {
        return false;
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error creating HMAC signature", e);
        }
    }
}
