package com.example.lazyco.backend.core.Cache;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class TimedECacheLRU<T> implements TimedCache<String, T> {

  /**
   * A simple LRU cache implementation using Ehcache with a time-to-live (TTL) policy. This cache
   * stores entries for a specified duration and evicts them based on the least recently used policy
   * when the cache size exceeds the maximum limit. The cache is thread-safe and can be used in
   * concurrent environments. It provides methods to get, put, invalidate, and clear cache entries.
   * It also tracks cache hits and misses for performance monitoring.
   */
  private final Cache<String, T> cache;

  private final AtomicLong hits = new AtomicLong(0);
  private final AtomicLong misses = new AtomicLong(0);

  // Constructor now accepts Class<T> and simplifies usage
  public TimedECacheLRU(Class<T> valueType, Duration ttl, int maxSize) {
    this(valueType.getSimpleName(), valueType, ttl, maxSize);
  }

  // Constructor to initialize the cache using Ehcache
  public TimedECacheLRU(String cacheName, Class<T> valueType, Duration ttl, int maxSize) {
    Cache<String, T> cache;
    try (CacheManager cacheManager =
        CacheManagerBuilder.newCacheManagerBuilder()
            .withCache(
                cacheName,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, valueType, ResourcePoolsBuilder.heap(maxSize))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl)))
            .build(true)) {
      cache = cacheManager.getCache(cacheName, String.class, valueType);
    } catch (Exception e) {
      ApplicationLogger.error("Error in initializing cache", e.getClass());
      cache = null;
    }
    this.cache = cache;
  }

  public T get(String key) {
    if (cache != null) {
      // Check if the value is in the cache
      T value = cache.get(key);
      if (value != null) {
        hits.incrementAndGet();
        return value;
      }
    }
    misses.incrementAndGet();
    return null;
  }

  // Fetch the value from cache or database
  public T get(String key, Supplier<T> dbFetcher) {
    if (cache != null) {
      // Check if the value is in the cache
      T value = cache.get(key);
      if (value != null) {
        hits.incrementAndGet();
        return value;
      }
    }
    misses.incrementAndGet();

    // If not found in cache, fetch from the database
    T newValue = dbFetcher.get();
    if (newValue != null) {
      put(key, newValue);
    }
    return newValue;
  }

  // Put the value in the cache
  public void put(String key, T value) {
    if (cache != null) cache.put(key, value);
  }

  public boolean containsKey(String key) {
    if (cache != null) {
      return cache.get(key) != null;
    }
    return false;
  }

  // Invalidate the cache entry
  public void remove(String key) {
    if (cache != null) cache.remove(key);
  }

  // Clear the entire cache
  public void clear() {
    if (cache != null) cache.clear();
  }

  // Get the number of cache hits
  public long getHits() {
    return hits.get();
  }

  // Get the number of cache misses
  public long getMisses() {
    return misses.get();
  }

  public long getSize() {
    if (cache != null) {
      return cache.getRuntimeConfiguration().getResourcePools().getResourceTypeSet().size();
    }
    return 0;
  }
}
