package com.kekeek.travel.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SimpleCacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {
 
    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(Collections.singletonList("pages"));
    }
}