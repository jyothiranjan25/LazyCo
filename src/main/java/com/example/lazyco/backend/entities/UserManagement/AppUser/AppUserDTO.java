package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AppUser.class)
public class AppUserDTO extends AbstractDTO<AppUserDTO> {

  @InternalFilterableField
  private String userId;

  @InternalFilterableField
  private String password;

  @InternalFilterableField private String email;

  @InternalFilterableField private String firstName;

  @InternalFilterableField private String lastName;

  private List<CRUDEnums> permissions;
}
