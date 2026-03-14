package com.example.lazyco.entities.SyllabusMaster;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ProgramCycle.ProgramCycle;
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
    name = "syllabus_master",
    comment = "table to store syllabus master information",
    indexes = {
      @Index(name = "idx_syllabus_master_code", columnList = "code"),
      @Index(name = "idx_syllabus_master_name", columnList = "name")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_syllabus_master_code", columnNames = "code")})
@EntityListeners(SyllabusMasterListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SyllabusMaster extends AbstractRBACModel {

  @Column(name = "code", nullable = false, comment = "unique code for the syllabus master")
  private String code;

  @Column(name = "name", nullable = false, comment = "name of the syllabus master")
  private String name;

  @Column(name = "description", comment = "description of the syllabus master")
  private String description;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default false",
      comment = "indicates if the syllabus master is active")
  private Boolean isActive;

  @OneToMany(mappedBy = "syllabusMaster")
  private Set<ProgramCycle> programCycles;
}
