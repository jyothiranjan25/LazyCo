package com.example.lazyco.backend.core.DatabaseConf;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecondLevelCacheConfig {

  @Bean
  public CacheManager jCacheManager() {
    CacheConfiguration<Object, Object> cacheConfiguration =
        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class, // Key type - Object allows any key type
                Object.class, // Value type - Object allows any value type
                ResourcePoolsBuilder.heap(1000) // Store up to 1000 entries in heap memory
                )
            // Set cache entries to expire after 15 minutes of creation
            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(java.time.Duration.ofMinutes(15)))
            .build();

    // Convert Ehcache configuration to JCache (JSR-107) configuration
    javax.cache.configuration.Configuration<Object, Object> jcacheConfig =
        Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);

    // Get the JCache provider (Ehcache implementation)
    CachingProvider provider = Caching.getCachingProvider();
    CacheManager cacheManager = provider.getCacheManager();

    // Create Hibernate second-level cache
    if (cacheManager.getCache("hibernateCache") == null) {
      cacheManager.createCache("hibernateCache", jcacheConfig);
    }

    return cacheManager;
  }
}
