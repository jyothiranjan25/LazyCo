package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
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
    name = "academic_program",
    comment = "Table storing academic program details",
    indexes = {
      @Index(name = "idx_academic_program_code", columnList = "code"),
      @Index(name = "idx_academic_program_name", columnList = "name"),
      @Index(name = "idx_academic_program_program_study_mode", columnList = "program_study_mode"),
      @Index(name = "idx_academic_program_program_study_type", columnList = "program_study_type")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_academic_program_code", columnNames = "code")
    })
@EntityListeners(AcademicProgramListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AcademicProgram extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the academic program")
  private String code;

  @Column(name = "name", comment = "Name of the academic program")
  private String name;

  @Column(name = "description", comment = "Description of the academic program")
  private String description;

  @Column(name = "program_study_mode", comment = "Study mode of the academic program")
  @Enumerated(EnumType.STRING)
  private ProgramStudyMode programStudyMode;

  @Column(name = "program_study_type", comment = "Study type of the academic program")
  @Enumerated(EnumType.STRING)
  private ProgramStudyType programStudyType;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Whether this academic program is active")
  private Boolean isActive;
}
