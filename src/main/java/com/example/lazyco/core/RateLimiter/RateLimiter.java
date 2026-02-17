package com.example.lazyco.core.RateLimiter;

import com.example.lazyco.core.Cache.TimedECacheLRU;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

  private final TimedECacheLRU<Bucket> rateLimiterCache =
      new TimedECacheLRU<>("rate-limiter-cache", Bucket.class, Duration.ofHours(1), 10000);

  private Bucket newTestBucket() {
    Bandwidth shortLimit =
        Bandwidth.builder().capacity(2).refillGreedy(2, Duration.ofSeconds(5)).build();

    return Bucket.builder().addLimit(shortLimit).build();
  }

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

  private Bucket newInternalBucket() {
    Bandwidth limit =
        Bandwidth.builder()
            .capacity(200) // 200 requests
            .refillGreedy(200, Duration.ofMinutes(1)) // per minute
            .build();

    Bandwidth burstLimit =
        Bandwidth.builder()
            .capacity(2000) // 2000 requests
            .refillGreedy(2000, Duration.ofHours(1)) // per hour
            .build();

    return Bucket.builder().addLimit(limit).addLimit(burstLimit).build();
  }

  private Bucket resolveBucket(String key, boolean internal) {
    return rateLimiterCache.get(key, () -> internal ? newInternalBucket() : newPublicBucket());
  }

  public ConsumptionProbe tryConsumeTest(String ip) {
    Bucket bucket = rateLimiterCache.get("TEST_" + ip, this::newTestBucket);
    return bucket.tryConsumeAndReturnRemaining(1);
  }

  public ConsumptionProbe tryConsumePublic(String ip) {
    Bucket bucket = resolveBucket("PUB_" + ip, false);
    return bucket.tryConsumeAndReturnRemaining(1);
  }

  public ConsumptionProbe tryConsumeInternal(String userId) {
    Bucket bucket = resolveBucket("INT_" + userId, true);
    return bucket.tryConsumeAndReturnRemaining(1);
  }
}
