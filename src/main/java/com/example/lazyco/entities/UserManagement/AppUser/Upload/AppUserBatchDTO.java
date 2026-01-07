package com.example.lazyco.entities.UserManagement.AppUser.Upload;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.BatchJob.SpringBatch.AbstractBatchDTO;
import com.example.lazyco.core.WebMVC.RequestHandling.CSVParams.CsvField;
import com.example.lazyco.entities.UserManagement.AppUser.AppUser;
import com.example.lazyco.entities.UserManagement.AppUser.AuthorityEnum;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AppUser.class)
public class AppUserBatchDTO extends AbstractBatchDTO<AppUserBatchDTO> {

  @CsvField(order = 1)
  private String userId;

  @CsvField(order = 2)
  private String email;

  @CsvField(order = 3)
  private String password;

  @CsvField(order = 5)
  private String firstName;

  @CsvField(order = 6)
  private String lastName;

  @CsvField(order = 8)
  private List<AuthorityEnum> authorities;
}
