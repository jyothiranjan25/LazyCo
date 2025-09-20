package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AppUser.class)
public class AppUserDTO extends AbstractDTO<AppUserDTO> {

  @CsvField private String userId;

  private String password;

  private String email;

  private String firstName;

  private String lastName;

  private List<CRUDEnums> permissions;

  private Date test;
}
