package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModelBase;
import com.example.lazyco.backend.schema.database.AppUserSchema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
    name = AppUserSchema.TABLE_NAME,
    indexes = {
      @Index(name = "idx_app_user_user_id", columnList = AppUserSchema.USER_ID),
      @Index(name = "idx_app_user_email", columnList = AppUserSchema.EMAIL),
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_app_user_user_id",
          columnNames = {AppUserSchema.USER_ID}),
      @UniqueConstraint(
          name = "uk_app_user_email",
          columnNames = {AppUserSchema.EMAIL})
    })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppUser extends AbstractModelBase {

  @Column(name = AppUserSchema.USER_ID)
  private String userId;

  @Column(name = AppUserSchema.PASSWORD)
  private String password;

  @Column(name = AppUserSchema.EMAIL)
  private String email;

  @Column(name = AppUserSchema.FIRST_NAME)
  private String firstName;

  @Column(name = AppUserSchema.LAST_NAME)
  private String lastName;
}
