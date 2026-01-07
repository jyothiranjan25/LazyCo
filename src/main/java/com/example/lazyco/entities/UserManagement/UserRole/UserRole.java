package com.example.lazyco.backend.entities.UserManagement.UserRole;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUser;
import com.example.lazyco.backend.entities.UserManagement.Role.Role;
import com.example.lazyco.backend.entities.UserManagement.UserGroup.UserGroup;
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
    name = "user_role",
    comment = "Table storing user roles in the system",
    indexes = {
      @Index(name = "idx_user_role_app_user_id", columnList = "app_user_id"),
      @Index(name = "idx_user_role_role_id", columnList = "role_id"),
      @Index(name = "idx_user_role_user_group_id", columnList = "user_group_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_user_role_app_user_role_user_group",
          columnNames = {"app_user_id", "role_id", "user_group_id"})
    })
@EntityListeners(UserRoleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserRole extends AbstractModel {

  @ManyToOne
  @JoinColumn(name = "app_user_id", comment = "Reference to the user")
  private AppUser appUser;

  @ManyToOne
  @JoinColumn(name = "role_id", comment = "Reference to the role")
  private Role role;

  @ManyToOne
  @JoinColumn(name = "user_group_id", comment = "Reference to the user group")
  private UserGroup userGroup;
}
