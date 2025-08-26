package com.example.lazyco.backend.entities.UserManagement.AppUser;

import java.util.List;

import com.example.lazyco.backend.core.AbstractClasses.JpaRepository.AbstractJpaRepository;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
public class AppUserService extends AbstractService<AppUserDTO, AppUser> {

    protected AppUserService(AppUserMapper appUserMapper, AppUserRepository appUserRepository) {
        super(appUserMapper, appUserRepository);
    }

    @Override
    protected void validateBeforeCreateOrUpdate(AppUserDTO dtoToCheck) {

    }
}
