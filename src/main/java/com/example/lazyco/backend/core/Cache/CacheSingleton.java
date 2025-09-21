package com.example.lazyco.backend.core.Cache;

import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import java.time.Duration;
import lombok.Getter;

public class CacheSingleton {

  // the TTL for the cache
  private static final Duration TTL = Duration.ofMinutes(10);
  // the estimate entities in the cache
  private static final int MAX_CACHE_SIZE = 100000;

  @Getter
  private static final TimedECacheLRU<AppUserDTO> appUserCache =
      new TimedECacheLRU<>(AppUserDTO.class, TTL, MAX_CACHE_SIZE);
}
