package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import com.example.lazyco.backend.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AppUser.class)
public class AppUserDTO extends AbstractDTO<AppUserDTO> {

  @SerializedName("user_id")
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
  @CsvField(order = 4)
  private CRUDEnums crudEnum;

  @InternalFilterableField private String lastName;

  private List<CRUDEnums> permissions;

  private Date test;
}
