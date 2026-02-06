package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Filter.FilterableField;
import com.example.lazyco.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.google.gson.annotations.Expose;
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

  @Expose(serialize = false)
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

  @CsvField(order = 8)
  @FilterableField
  private List<AuthorityEnum> authorities;

  private Boolean isSuperAdmin;

  private Boolean isAdministrator;

  private Boolean isActive;

  private Boolean isLocked;

  private String resetPasswordToken;

  private Date resetPasswordTokenExpiry;

  private Boolean clearResetPasswordToken;

  private Date lastLoginDate;

  private String lastLoginIpAddress;
}
