package com.example.lazyco.backend.core.Cache;

import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
