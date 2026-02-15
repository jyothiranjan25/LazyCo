package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.ProgramSpecialization.ProgramSpecialization;
import com.example.lazyco.entities.AcademicProgram.ProgramTermSystem.ProgramTermSystem;
import com.example.lazyco.entities.Institution.Institution;
import com.example.lazyco.entities.ProgramCurriculum.ProgramCurriculum;
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
    name = "academic_program",
    comment = "Table storing academic program details",
    indexes = {
      @Index(name = "idx_academic_program_code", columnList = "code"),
      @Index(name = "idx_academic_program_name", columnList = "name"),
      @Index(name = "idx_academic_program_program_study_mode", columnList = "program_study_mode"),
      @Index(name = "idx_academic_program_program_level", columnList = "program_level"),
      @Index(name = "idx_academic_program_is_active", columnList = "is_active"),
      @Index(name = "idx_academic_program_institution_id", columnList = "institution_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_academic_program_code", columnNames = "code"),
      @UniqueConstraint(
          name = "uk_academic_program_code_institution",
          columnNames = {"code", "institution_id"})
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
  private ProgramStudyModeEnum programStudyMode;

  @Column(name = "program_level", comment = "Level of the academic program")
  @Enumerated(EnumType.STRING)
  private ProgramLevelEnum programLevel;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Whether this academic program is active")
  private Boolean isActive;

  @ManyToOne
  @JoinColumn(
      name = "institution_id",
      foreignKey = @ForeignKey(name = "fk_academic_program_institution"),
      nullable = false,
      comment = "Reference to the institution offering this academic program")
  private Institution institution;

  @OneToMany(mappedBy = "academicProgram")
  private Set<ProgramSpecialization> programSpecializations;

  @OneToMany(mappedBy = "academicProgram")
  private Set<ProgramTermSystem> programTermSystems;

  @OneToMany(mappedBy = "academicProgram")
  private Set<ProgramCurriculum> programCurriculums;
}
