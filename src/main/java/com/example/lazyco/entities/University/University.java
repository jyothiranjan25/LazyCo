package com.example.lazyco.entities.University;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.Institution.Institution;
import jakarta.persistence.*;
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
    name = "university",
    comment = "Table storing universities",
    indexes = {
      @Index(name = "idx_university_code", columnList = "code"),
      @Index(name = "idx_university_name", columnList = "name"),
      @Index(name = "idx_university_is_active", columnList = "is_active")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_university_code", columnNames = "code")})
@EntityListeners(UniversityListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class University extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the university")
  private String code;

  @Column(name = "name", comment = "Name of the university")
  private String name;

  @Column(name = "description", comment = "Description of the university")
  private String description;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Whether this university is active")
  private Boolean isActive;

  @OneToMany(mappedBy = "university")
  private Set<Institution> institutions;
}
