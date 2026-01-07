package com.example.lazyco.core.Cache;

import java.util.function.Supplier;

public interface TimedCache<K, T> {

  public T get(K key);

  public T get(K key, Supplier<T> dbFetcher);

  public void put(K key, T value);

  public boolean containsKey(K key);

  public void remove(K key);

  public void clear();

  public long getHits();

  public long getMisses();

  public long getSize();
}
