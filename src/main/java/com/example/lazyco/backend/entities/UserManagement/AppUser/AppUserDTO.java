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

  @CsvField(order = 1)
  private String userId;

  @CsvField(order = 3)
  private String password;

  @CsvField(order = 2)
  private String email;

  @CsvField(order = 4)
  private String firstName;

  private String lastName;

  private List<CRUDEnums> permissions;

  private Date test;
}
