package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.AcademicProgram;
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
    name = "program_specialization",
    comment = "Table storing program specialization details",
    indexes = {
      @Index(name = "idx_program_specialization_code", columnList = "code"),
      @Index(name = "idx_program_specialization_name", columnList = "name"),
      @Index(name = "idx_program_specialization_is_active", columnList = "is_active"),
      @Index(
          name = "idx_program_specialization_academic_program_id",
          columnList = "academic_program_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_program_specialization_code", columnNames = "code"),
    })
@EntityListeners(ProgramSpecializationListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramSpecialization extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the program specialization")
  private String code;

  @Column(name = "name", comment = "Name of the program specialization")
  private String name;

  @Column(name = "description", comment = "Description of the program specialization")
  private String description;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Whether this program specialization is active or not")
  private Boolean isActive;

  @ManyToOne
  @JoinColumn(
      name = "academic_program_id",
      foreignKey = @ForeignKey(name = "fk_program_specialization_academic_program"),
      nullable = false,
      comment = "Foreign key referencing the academic program")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private AcademicProgram academicProgram;
}
