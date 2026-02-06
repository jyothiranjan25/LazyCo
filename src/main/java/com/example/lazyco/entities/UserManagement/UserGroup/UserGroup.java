package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
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
    name = "user_group",
    comment = "Table storing user group details",
    indexes = {
      @Index(name = "idx_user_group_name", columnList = "user_group_name"),
      @Index(name = "idx_user_group_fully_qualified_name", columnList = "fully_qualified_name"),
      @Index(name = "idx_user_group_parent_user_group_id", columnList = "parent_user_group_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_user_group_name", columnNames = "user_group_name"),
      @UniqueConstraint(
          name = "uk_user_group_fully_qualified_name",
          columnNames = {"fully_qualified_name"})
    })
@EntityListeners(UserGroupListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserGroup extends AbstractModel {

  @Column(name = "name", comment = "Unique user group name")
  private String userGroupName;

  @Column(name = "fully_qualified_name", comment = "Fully qualified user group name")
  private String fullyQualifiedName;

  @Column(name = "description", comment = "Description of the user group")
  private String description;

  @ManyToOne
  @JoinColumn(
      name = "parent_user_group_id",
      foreignKey = @ForeignKey(name = "fk_user_group_parent_user_group"),
      comment = "Reference to the parent user group")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserGroup parentUserGroup;

  private Set<UserGroup> childUserGroups;
}
