package com.kodsonApp.configuration;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class DotEnvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    public static final String PROPERTY_SOURCE_NAME = "dotenv";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> properties = DotEnvLoader.loadProperties();
        if (properties.isEmpty()) {
            return;
        }

        MapPropertySource source = new MapPropertySource(PROPERTY_SOURCE_NAME, properties);
        if (environment.getPropertySources().contains("systemEnvironment")) {
            environment.getPropertySources().addAfter("systemEnvironment", source);
        } else {
            environment.getPropertySources().addFirst(source);
        }
    }
}
