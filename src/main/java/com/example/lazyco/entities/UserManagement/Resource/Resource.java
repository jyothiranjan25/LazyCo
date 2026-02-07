package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.entities.UserManagement.Module.Module;
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
    name = "resource",
    comment = "Table storing application resources",
    indexes = {
      @Index(name = "idx_resource_name", columnList = "resource_name"),
      @Index(name = "idx_resource_order", columnList = "resource_order"),
      @Index(name = "idx_resource_parent_resource_id", columnList = "parent_resource_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_resource_name", columnNames = "resource_name")
    })
@EntityListeners(ResourceListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends AbstractModel {

  @Column(name = "name", comment = "Name of the resource")
  private String resourceName;

  @Column(name = "description", comment = "Description of the resource")
  private String description;

  @Column(name = "resource_order", comment = "Order of the resource for sorting purposes")
  private Integer resourceOrder;

  @Column(name = "action", comment = "Action associated with the resource")
  private String action;

  @Column(
      name = "show_in_menu",
      columnDefinition = "boolean default true",
      comment = "Whether this resource should be visible")
  private Boolean showInMenu;

  @ManyToOne
  @JoinColumn(
      name = "parent_resource_id",
      foreignKey = @ForeignKey(name = "fk_resource_parent_resource"),
      comment = "Reference to the parent resource")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Resource parentResource;

  @OneToMany(mappedBy = "parentResource")
  private Set<Resource> childResources;

  @ManyToMany(mappedBy = "resources")
  private Set<Module> modules;

  @OneToMany(mappedBy = "resource")
  private Set<RoleModuleResource> roleModuleResources;
}
