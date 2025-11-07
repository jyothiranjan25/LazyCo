package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Filter.FilterableField;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AppUser.class)
public class AppUserDTO extends AbstractDTO<AppUserDTO> {

  @FilterableField
  @InternalFilterableField
  @CsvField(order = 1)
  private String userId;

  @InternalFilterableField
  @CsvField(order = 3)
  private String password;

  @InternalFilterableField
  @CsvField(order = 2)
  private String email;

  @InternalFilterableField
  @CsvField(order = 5)
  private String firstName;

  @InternalFilterableField
  @CsvField(order = 6)
  private String lastName;

  @CsvField(order = 7)
  private Date lastLogin;

  @CsvField(order = 8)
  @FilterableField
  private List<AuthorityEntityEnum> authorities;
}
