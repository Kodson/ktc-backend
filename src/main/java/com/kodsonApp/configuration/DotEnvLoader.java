package com.kodsonApp.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class DotEnvLoader {

    private static final List<String> ENV_TO_PROPERTY = List.of(
            "SPRING_DATASOURCE_URL", "spring.datasource.url",
            "DATABASE_URL", "spring.datasource.url",
            "SPRING_DATASOURCE_USERNAME", "spring.datasource.username",
            "DATABASE_USERNAME", "spring.datasource.username",
            "SPRING_DATASOURCE_PASSWORD", "spring.datasource.password",
            "DATABASE_PASSWORD", "spring.datasource.password",
            "SPRING_PROFILES_ACTIVE", "spring.profiles.active",
            "SERVER_PORT", "server.port",
            "JWT_SECRET", "jwt.secret",
            "MAIL_HOST", "mail.host",
            "MAIL_PORT", "mail.port",
            "MAIL_USERNAME", "mail.username",
            "MAIL_PASSWORD", "mail.password"
    );

    private DotEnvLoader() {
    }

    public static Optional<Path> findEnvFile() {
        for (String candidate : List.of(".env", "/app/.env")) {
            Path path = Path.of(candidate);
            if (Files.isRegularFile(path)) {
                return Optional.of(path.toAbsolutePath().normalize());
            }
        }
        return Optional.empty();
    }

    public static Map<String, Object> loadProperties() {
        Optional<Path> envFile = findEnvFile();
        if (envFile.isEmpty()) {
            return Map.of();
        }

        Map<String, String> envKeyToProperty = new LinkedHashMap<>();
        for (int i = 0; i < ENV_TO_PROPERTY.size(); i += 2) {
            envKeyToProperty.put(ENV_TO_PROPERTY.get(i), ENV_TO_PROPERTY.get(i + 1));
        }

        Map<String, Object> properties = new LinkedHashMap<>();
        try {
            for (String line : Files.readAllLines(envFile.get())) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int equals = line.indexOf('=');
                if (equals <= 0) {
                    continue;
                }
                String envKey = line.substring(0, equals).trim();
                String value = line.substring(equals + 1).trim();
                if (System.getenv(envKey) != null) {
                    continue;
                }
                String propertyKey = envKeyToProperty.getOrDefault(envKey, envKey);
                properties.putIfAbsent(propertyKey, value);
            }
        } catch (IOException ignored) {
            return Map.of();
        }
        return properties;
    }
}
