package com.example.lazyco.core.RateLimiter;

import com.example.lazyco.core.Cache.TimedECacheLRU;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.function.Supplier;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

  private final CacheManager cacheManager;
  private final Cache<String, Bucket> rateLimiterSecurityCache;

  public RateLimiter() {
    this.cacheManager =
        CacheManagerBuilder.newCacheManagerBuilder()
            .withCache(
                "security-rate-limit-cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, Bucket.class, ResourcePoolsBuilder.heap(10_000))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(1))))
            .build(true);

    this.rateLimiterSecurityCache =
        cacheManager.getCache("security-rate-limit-cache", String.class, Bucket.class);
  }

  @PreDestroy
  public void shutdown() {
    cacheManager.close();
  }

  private Bucket newSecurityBucket() {
    return Bucket.builder()
        .addLimit(Bandwidth.builder().capacity(1).refillGreedy(1, Duration.ofMinutes(30)).build())
        .build();
  }

  public ConsumptionProbe tryConsumeSecurity(String ip) {
    Bucket bucket = securityCache("SEC_" + ip, this::newSecurityBucket);
    return bucket.tryConsumeAndReturnRemaining(1);
  }

  private Bucket securityCache(String key, Supplier<Bucket> supplier) {

    Bucket bucket = rateLimiterSecurityCache.get(key);

    if (bucket == null) {
      bucket = supplier.get();
      rateLimiterSecurityCache.put(key, bucket);
    }

    return bucket;
  }

  /** */
  private final TimedECacheLRU<Bucket> rateLimiterCache =
      new TimedECacheLRU<>("rate-limiter-cache", Bucket.class, Duration.ofHours(1), 10000);

  // Bucket for public endpoints: 50 requests per minute, burst up to 500 per hour
  private Bucket newPublicBucket() {
    Bandwidth limit =
        Bandwidth.builder()
            .capacity(50) // 50 requests
            .refillGreedy(50, Duration.ofMinutes(1)) // per minute
            .build();

    Bandwidth burstLimit =
        Bandwidth.builder()
            .capacity(500) // 500 requests
            .refillGreedy(500, Duration.ofHours(1)) // per hour
            .build();

    return Bucket.builder().addLimit(limit).addLimit(burstLimit).build();
  }

  // Bucket for internal endpoints: 300 requests per minute, burst up to 5000 per hour
  private Bucket newInternalBucket() {
    Bandwidth limit =
        Bandwidth.builder()
            .capacity(300) // 200 requests
            .refillGreedy(300, Duration.ofMinutes(1)) // per minute
            .build();

    Bandwidth burstLimit =
        Bandwidth.builder()
            .capacity(5000) // 2000 requests
            .refillGreedy(5000, Duration.ofHours(1)) // per hour
            .build();

    return Bucket.builder().addLimit(limit).addLimit(burstLimit).build();
  }

  // Bucket for login endpoints: 10 requests per minute, no burst allowed
  private Bucket newLoginBucket() {
    Bandwidth limit =
        Bandwidth.builder()
            .capacity(10) // 10 requests
            .refillGreedy(10, Duration.ofMinutes(1)) // per minute
            .build();

    return Bucket.builder().addLimit(limit).build();
  }

  // Simple bucket for testing: 2 requests per 5 seconds
  private Bucket newTestBucket() {
    Bandwidth shortLimit =
        Bandwidth.builder().capacity(2).refillGreedy(2, Duration.ofSeconds(5)).build();

    return Bucket.builder().addLimit(shortLimit).build();
  }

  public ConsumptionProbe tryConsumePublic(String ip) {
    Bucket bucket = rateLimiterCache.get("PUB_" + ip, this::newPublicBucket);
    return bucket.tryConsumeAndReturnRemaining(1);
  }

  public ConsumptionProbe tryConsumeInternal(String userId) {
    Bucket bucket = rateLimiterCache.get("INT_" + userId, this::newInternalBucket);
    return bucket.tryConsumeAndReturnRemaining(1);
  }

  public ConsumptionProbe tryConsumeLogin(String ip) {
    Bucket bucket = rateLimiterCache.get("LOGIN_" + ip, this::newLoginBucket);
    return bucket.tryConsumeAndReturnRemaining(1);
  }

  public ConsumptionProbe tryConsumeTest(String ip) {
    Bucket bucket = rateLimiterCache.get("TEST_" + ip, this::newTestBucket);
    return bucket.tryConsumeAndReturnRemaining(1);
  }
}
