package com.kodsonApp.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.nio.file.Files;
import java.nio.file.Path;

public class DatasourceConfigLogger implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger log = LoggerFactory.getLogger(DatasourceConfigLogger.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Environment env = event.getEnvironment();

        String[] active = env.getActiveProfiles();
        String profiles = active.length == 0 ? "default" : String.join(", ", active);

        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");

        log.info("=== Database connection configuration ===");
        log.info("Active profile(s): {}", profiles);
        log.info("spring.datasource.url: {}", url);
        log.info("spring.datasource.username: {}", username);
        if (runningInDocker() && usesLocalhostDatabase(url)) {
            log.warn(
                    "Postgres is on the Ubuntu HOST but this container uses bridge networking. "
                            + "127.0.0.1 inside the container is NOT the host. "
                            + "Recreate with: docker run --network host ... OR docker compose up (network_mode: host).");
        }
        log.info("=========================================");
    }

    private static boolean runningInDocker() {
        return Files.exists(Path.of("/.dockerenv"));
    }

    private static boolean usesLocalhostDatabase(String url) {
        if (url == null) {
            return false;
        }
        return url.contains("127.0.0.1") || url.contains("localhost");
    }
}
