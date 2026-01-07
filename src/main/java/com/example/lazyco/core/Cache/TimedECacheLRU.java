package com.example.lazyco.core.Cache;

import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
  private final CacheManager cacheManager;

  private final Cache<String, T> cache;

  private final AtomicLong hits = new AtomicLong(0);
  private final AtomicLong misses = new AtomicLong(0);

  // Static registry to manage multiple caches
  private static final Map<String, TimedECacheLRU<?>> CACHES = new ConcurrentHashMap<>();

  public static void register(String name, TimedECacheLRU<?> cache) {
    CACHES.put(name, cache);
  }

  // Constructor now accepts Class<T> and simplifies usage
  public TimedECacheLRU(Class<T> valueType, Duration ttl, int maxSize) {
    this(valueType.getSimpleName(), valueType, ttl, maxSize);
  }

  // Constructor to initialize the cache using Ehcache
  public TimedECacheLRU(String cacheName, Class<T> valueType, Duration ttl, int maxSize) {
    this.cacheManager =
        CacheManagerBuilder.newCacheManagerBuilder()
            .withCache(
                cacheName,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        String.class, valueType, ResourcePoolsBuilder.heap(maxSize))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl)))
            .build(true);
    this.cache = cacheManager.getCache(cacheName, String.class, valueType);

    // Register this cache instance in the CacheRegistry
    register(cacheName, this);
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

  // Clear all registered caches
  public static void clearAll() {
    CACHES.values().forEach(TimedECacheLRU::clear);
    CACHES.clear();
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

  @PreDestroy
  public void close() {
    if (cacheManager != null) {
      cacheManager.close();
    }
  }
}
