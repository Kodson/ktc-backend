package com.kodsonApp.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    @Bean
    @Override
    public CacheManager cacheManager() {
        // Use simple in-memory cache instead of Redis
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();

        // Pre-define cache names used in the application
        cacheManager.setCacheNames(
            java.util.Arrays.asList(
                "tanks",
                "supplies",
                "dailySales",
                "entities",
                "jwt-tokens"
            )
        );

        // Allow dynamic cache creation
        cacheManager.setAllowNullValues(false);

        return cacheManager;
    }
}
