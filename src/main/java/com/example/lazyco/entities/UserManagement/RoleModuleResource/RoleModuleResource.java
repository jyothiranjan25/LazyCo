package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.entities.UserManagement.Module.Module;
import com.example.lazyco.entities.UserManagement.Resource.Resource;
import com.example.lazyco.entities.UserManagement.Role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
    name = "role_module_resource",
    comment = "Table storing module resource for a role in the system",
    indexes = {
      @Index(name = "idx_role_module_resource_role_id", columnList = "role_id"),
      @Index(name = "idx_role_module_resource_module_id", columnList = "module_id"),
      @Index(name = "idx_role_module_resource_resource_id", columnList = "resource_id"),
      @Index(
          name = "idx_role_module_resource_role_module_resource",
          columnList = "role_id, module_id, resource_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_role_module_resource_role_module_resource",
          columnNames = {"role_id", "module_id", "resource_id"})
    })
@EntityListeners(RoleModuleResourceListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RoleModuleResource extends AbstractModel {

  @ManyToOne
  @JoinColumn(
      name = "role_id",
      foreignKey = @ForeignKey(name = "fk_role_module_resource_role"),
      comment = "Reference to the role")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Role role;

  @ManyToOne
  @JoinColumn(
      name = "module_id",
      foreignKey = @ForeignKey(name = "fk_role_module_resource_module"),
      comment = "Reference to the module")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Module module;

  @ManyToOne
  @JoinColumn(
      name = "resource_id",
      foreignKey = @ForeignKey(name = "fk_role_module_resource_resource"),
      comment = "Reference to the resource")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Resource resource;

  @Column(name = "display_order", comment = "Order of the module for sorting purposes")
  private Integer displayOrder;
}
