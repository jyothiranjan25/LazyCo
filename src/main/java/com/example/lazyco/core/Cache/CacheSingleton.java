package com.example.lazyco.core.Cache;

import com.example.lazyco.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.entities.UserManagement.UserRole.UserRoleDTO;
import java.time.Duration;
import java.util.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Singleton class that holds various caches used throughout the application. Each cache is
 * implemented using TimedECacheLRU with a specified TTL and maximum size. This class provides
 * static access to these caches. Don't use this cache for large objects or large collections, as it
 * is an in-memory cache.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheSingleton {

  // the TTL for the cache
  private static final Duration TTL = Duration.ofMinutes(10);
  // the estimate entities in the cache
  private static final int MAX_CACHE_SIZE = 100000;

  @Getter
  private static final TimedECacheLRU<AppUserDTO> appUserCache =
      new TimedECacheLRU<>(AppUserDTO.class, TTL, MAX_CACHE_SIZE);

  @Getter
  private static final TimedECacheLRU<UserRoleDTO> userRoleCache =
      new TimedECacheLRU<>(UserRoleDTO.class, TTL, MAX_CACHE_SIZE);

  public static class AppUserDTOList extends CachedList<AppUserDTO> {
    public AppUserDTOList(Collection<? extends AppUserDTO> c) {
      super(c);
    }
  }

  public static class AppUserDTOMap extends CachedMap<Long, AppUserDTO> {
    public AppUserDTOMap(Map<? extends Long, ? extends AppUserDTO> entries) {
      super(entries);
    }
  }

  // Helper classes for cached collections
  private abstract static class CachedList<T> extends ArrayList<T> {
    public CachedList(Collection<? extends T> c) {
      super(List.copyOf(c));
    }
  }

  private abstract static class CachedMap<T, V> extends HashMap<T, V> {
    public CachedMap(Map<? extends T, ? extends V> entries) {
      super(Map.copyOf(entries));
    }
  }
}
