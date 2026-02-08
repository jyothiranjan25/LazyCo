package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.entities.UserManagement.Resource.Resource;
import com.example.lazyco.entities.UserManagement.RoleModuleResource.RoleModuleResource;
import jakarta.persistence.*;
import java.util.Set;
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
    name = "module",
    comment = "Table storing modules in the system",
    indexes = {@Index(name = "idx_module_name", columnList = "name")},
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_module_name",
          columnNames = {"name"})
    })
@EntityListeners(ModuleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Module extends AbstractModel {

  @Column(name = "name", comment = "Name of the module")
  private String name;

  @Column(name = "description", comment = "Description of the module")
  private String description;

  @Column(name = "action", comment = "Action associated with the module")
  private String action;

  @Column(
      name = "show_in_menu",
      columnDefinition = "boolean default true",
      comment = "Whether this module should be visible")
  private Boolean showInMenu;

  @Column(name = "icon", comment = "Icon representing the module", length = 4069)
  private String icon;

  @ManyToMany
  @JoinTable(
      name = "module_resource",
      joinColumns =
          @JoinColumn(
              name = "module_id",
              foreignKey = @ForeignKey(name = "fk_module_resource_module"),
              comment = "Reference to the module"),
      inverseJoinColumns =
          @JoinColumn(
              name = "resource_id",
              foreignKey = @ForeignKey(name = "fk_module_resource_resource"),
              comment = "Reference to the resource"),
      indexes = {
        @Index(name = "idx_module_resource_module_id", columnList = "module_id"),
        @Index(name = "idx_module_resource_resource_id", columnList = "resource_id"),
        @Index(name = "idx_module_resource_module_resource", columnList = "module_id, resource_id")
      },
      uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_module_resource_module_resource",
            columnNames = {"module_id", "resource_id"})
      },
      comment = "Join table linking modules and resources")
  private Set<Resource> resources;

  @OneToMany(mappedBy = "module")
  private Set<RoleModuleResource> roleModuleResources;
}
