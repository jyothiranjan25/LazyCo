package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import jakarta.persistence.*;
import java.util.Date;
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

  @Column(name = "user_id", comment = "Unique user identifier")
  private String userId;

  @Column(name = "password", comment = "Hashed user password")
  private String password;

  @Column(name = "email", comment = "User email address")
  private String email;

  @Column(name = "first_name", comment = "User first name")
  private String firstName;

  @Column(name = "last_name", comment = "User last name")
  private String lastName;

  @Column(name = "last_login", comment = "Timestamp of the last user login")
  private Date lastLogin;

  @Column(name = "ip_address", comment = "User IP address")
  private String ipAddress;

  @Enumerated(EnumType.STRING)
  @Column(name = "authorities", comment = "User authorities/roles")
  private List<AuthorityEnum> authorities;
}
