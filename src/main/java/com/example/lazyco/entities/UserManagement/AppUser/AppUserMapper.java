package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper extends AbstractMapper<AppUserDTO, AppUser> {}
