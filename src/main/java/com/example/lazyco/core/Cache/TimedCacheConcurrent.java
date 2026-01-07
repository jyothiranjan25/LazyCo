package com.example.lazyco.core.Cache;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class TimedCacheConcurrent<K, T> implements TimedCache<K, T> {

  private static class CacheEntry<T> {
    T value;
    long timestamp;

    CacheEntry(T value, long timestamp) {
      this.value = value;
      this.timestamp = timestamp;
    }
  }

  private final AtomicLong hits = new AtomicLong();
  private final AtomicLong misses = new AtomicLong();
  private final AtomicLong lastCleanupTimestamp = new AtomicLong(0);

  private final long ttlMillis;
  private final ConcurrentHashMap<K, CacheEntry<T>> cache = new ConcurrentHashMap<>();

  public TimedCacheConcurrent() {
    this.ttlMillis = -1;
  }

  public TimedCacheConcurrent(Duration ttl) {
    this.ttlMillis = ttl.toMillis();
  }

  public T get(K key) {
    long last = lastCleanupTimestamp.get();
    long now = System.currentTimeMillis();

    if (now - last >= ttlMillis) {
      if (lastCleanupTimestamp.compareAndSet(last, now)) {
        cleanup();
      }
    }

    CacheEntry<T> entry = cache.get(key);
    if (entry != null && (ttlMillis == -1 || now - entry.timestamp < ttlMillis)) {
      hits.incrementAndGet();
      return entry.value;
    }

    misses.incrementAndGet();
    return null;
  }

  public T get(K key, Supplier<T> dbFetcher) {
    long last = lastCleanupTimestamp.get();
    long now = System.currentTimeMillis();

    if (now - last >= ttlMillis) {
      if (lastCleanupTimestamp.compareAndSet(last, now)) {
        cleanup();
      }
    }

    CacheEntry<T> entry = cache.get(key);
    if (entry != null && (ttlMillis == -1 || now - entry.timestamp < ttlMillis)) {
      hits.incrementAndGet();
      return entry.value;
    }

    misses.incrementAndGet();
    T newValue = dbFetcher.get();
    if (newValue != null) {
      cache.put(key, new CacheEntry<>(newValue, now));
    }
    return newValue;
  }

  public void put(K key, T value) {
    cache.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
  }

  public boolean containsKey(K key) {
    return cache.containsKey(key);
  }

  public void remove(K key) {
    cache.remove(key);
  }

  public void clear() {
    cache.clear();
  }

  public void forEach(BiConsumer<K, T> action) {
    cache.forEach(
        (key, entry) -> {
          if (entry != null) {
            action.accept(key, entry.value);
          }
        });
  }

  public void cleanup() {
    if (ttlMillis == -1) return;
    long now = System.currentTimeMillis();
    for (K key : cache.keySet()) {
      CacheEntry<T> entry = cache.get(key);
      if (entry != null && now - entry.timestamp >= ttlMillis) {
        cache.remove(key);
      }
    }
  }

  public long getHits() {
    return hits.get();
  }

  public long getMisses() {
    return misses.get();
  }

  public long getSize() {
    return cache.size();
  }
}
