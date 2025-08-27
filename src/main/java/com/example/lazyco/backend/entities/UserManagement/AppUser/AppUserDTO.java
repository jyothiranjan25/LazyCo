package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO extends AbstractDTO<AppUserDTO> {
  private String userId;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private List<CRUDEnums> permissions;
}
