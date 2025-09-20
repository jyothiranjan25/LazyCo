package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import jakarta.persistence.*;
import java.util.List;
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
    name = "app_user",
    comment = "Table storing application user details",
    indexes = {
      @Index(name = "idx_app_user_user_id", columnList = "user_id"),
      @Index(name = "idx_app_user_email", columnList = "email"),
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_app_user_user_id", columnNames = "user_id"),
      @UniqueConstraint(
          name = "uk_app_user_email",
          columnNames = {"email"})
    })
@EntityListeners(AppUserListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppUser extends AbstractRBACModel {

  @Column(name = "user_id", length = 50, comment = "Unique user identifier")
  private String userId;

  @Column(name = "password", comment = "Hashed user password")
  private String password;

  @Column(name = "email", length = 100, comment = "User email address")
  private String email;

  @Column(name = "first_name", length = 50, comment = "User first name")
  private String firstName;

  @Column(name = "last_name", length = 50, comment = "User last name")
  private String lastName;

  @Enumerated(EnumType.STRING)
  private List<CRUDEnums> permissions;
}
