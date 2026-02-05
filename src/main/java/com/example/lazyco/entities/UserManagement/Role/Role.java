package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
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
    name = "role",
    comment = "Table storing roles in the system",
    indexes = {@Index(name = "idx_role_name", columnList = "name")},
    uniqueConstraints = {@UniqueConstraint(name = "uk_role_name", columnNames = "name")})
@EntityListeners(RoleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends AbstractModel {

  @Column(name = "name", comment = "Name of the role")
  private String roleName;

  @Column(name = "description", comment = "Description of the role")
  private String description;

  @Column(name = "icon", comment = "Icon representing the role", length = 4069)
  private String icon;

  @Column(name = "role_type", comment = "Type of the role")
  @Enumerated(EnumType.STRING)
  private RoleTypeEnum roleType;
}
