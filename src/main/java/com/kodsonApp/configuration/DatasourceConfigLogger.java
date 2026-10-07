package com.kodsonApp.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

public class DatasourceConfigLogger implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger log = LoggerFactory.getLogger(DatasourceConfigLogger.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Environment env = event.getEnvironment();

        String[] active = env.getActiveProfiles();
        String profiles = active.length == 0 ? "default" : String.join(", ", active);

        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");

        boolean dotenvLoaded = env instanceof ConfigurableEnvironment configurable
                && configurable.getPropertySources().contains(DotEnvEnvironmentPostProcessor.PROPERTY_SOURCE_NAME);
        String dotenvPath = DotEnvLoader.findEnvFile().map(Object::toString).orElse("(not found)");

        log.info("=== Database connection configuration ===");
        log.info("Active profile(s): {}", profiles);
        log.info("spring.datasource.url: {}", url);
        log.info("spring.datasource.username: {}", username);
        log.info(".env file path: {}", dotenvPath);
        log.info(".env property source loaded: {}", dotenvLoaded);
        log.info("SPRING_DATASOURCE_URL env var present: {}", System.getenv("SPRING_DATASOURCE_URL") != null);
        log.info("SPRING_PROFILES_ACTIVE env var: {}", System.getenv("SPRING_PROFILES_ACTIVE"));
        log.info("DATABASE_URL env var present: {}", System.getenv("DATABASE_URL") != null);
        log.info("=========================================");
    }
}
