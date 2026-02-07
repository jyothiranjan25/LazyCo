package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.UserManagement.UserRole.UserRole;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;
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
      @UniqueConstraint(name = "uk_app_user_email", columnNames = "email")
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

  @Enumerated(EnumType.STRING)
  @Column(name = "authorities", comment = "User authorities/roles")
  private Set<AuthorityEnum> authorities;

  @Column(
      name = "is_super_admin",
      comment = "Indicates if the user has super admin privileges",
      columnDefinition = "boolean default false")
  private Boolean isSuperAdmin;

  @Column(
      name = "is_administrator",
      comment = "Indicates if the user has administrator privileges",
      columnDefinition = "boolean default false")
  private Boolean isAdministrator;

  @Column(
      name = "is_active",
      comment = "Indicates if the user is active",
      columnDefinition = "boolean default true")
  private Boolean isActive;

  @Column(
      name = "is_locked",
      comment = "Indicates if the user account is locked",
      columnDefinition = "boolean default false")
  private Boolean isLocked;

  @Column(name = "reset_password_token", comment = "Token used for resetting the user password")
  private String resetPasswordToken;

  @Column(name = "reset_password_token_expiry", comment = "Reset password token expiry timestamp")
  private Date resetPasswordTokenExpiry;

  @Column(name = "last_login_date", comment = "Timestamp of the last user login")
  private Date lastLoginDate;

  @Column(name = "last_login_ip_address", comment = "IP address of the last user login")
  private String lastLoginIpAddress;

  @OneToMany(mappedBy = "appUser")
  private Set<UserRole> userRoles;
}
