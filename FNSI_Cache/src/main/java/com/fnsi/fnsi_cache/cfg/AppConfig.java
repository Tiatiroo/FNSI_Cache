package com.fnsi.fnsi_cache.cfg;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@Configuration
@EnableCaching
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AppConfig {
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Value("${cache.duration}")
    private Integer cacheDuration;
    @Bean()
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String dictionaryCache) {
                return new ConcurrentMapCache(dictionaryCache,
                        CacheBuilder.newBuilder().expireAfterWrite(cacheDuration, TimeUnit.SECONDS).build().asMap(),
                        false);
            }
        };
    }
}