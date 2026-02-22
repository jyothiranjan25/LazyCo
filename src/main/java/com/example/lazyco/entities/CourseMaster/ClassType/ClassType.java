package com.example.lazyco.entities.CourseMaster.ClassType;

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
    name = "class_type",
    comment = "table to store class type information",
    indexes = {@Index(name = "idx_class_type_name", columnList = "name")})
@EntityListeners(ClassTypeListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassType extends AbstractModel {

  @Column(name = "name", nullable = false, comment = "name of the class type")
  private String name;

  @Column(name = "description", comment = "description of the class type")
  private String description;
}
