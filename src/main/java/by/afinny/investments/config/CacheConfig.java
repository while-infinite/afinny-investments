package by.afinny.investments.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration("availableStocksCache",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(1)))
                .withCacheConfiguration("availableCurrenciesCache",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(1)));
    }

}
