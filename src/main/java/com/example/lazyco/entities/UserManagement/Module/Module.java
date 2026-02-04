package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.entities.UserManagement.Resource.Resource;
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
@Table(name = "module", comment = "Table storing modules in the system")
@EntityListeners(ModuleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Module extends AbstractModel {

  @Column(name = "module_name", comment = "Name of the module")
  private String moduleName;

  @Column(name = "description", comment = "Description of the module")
  private String moduleDescription;

  @Column(name = "action", comment = "Action associated with the module")
  private String action;

  @ManyToMany
  @JoinTable(
      name = "module_resource",
      joinColumns =
          @JoinColumn(
              name = "module_id",
              foreignKey = @ForeignKey(name = "fk_module_resource_module")),
      inverseJoinColumns =
          @JoinColumn(
              name = "resource_id",
              foreignKey = @ForeignKey(name = "fk_module_resource_resource")),
      comment = "Join table linking modules and resources")
  private Set<Resource> resources;
}
