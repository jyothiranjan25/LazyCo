package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.JpaRepository.AbstractJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends AbstractJpaRepository<AppUser> {}
