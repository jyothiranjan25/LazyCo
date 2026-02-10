package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.ProgramTermSystem.ProgramTermSystem;
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
    name = "program_term_master",
    comment = "Table storing program term master details",
    indexes = {
      @Index(name = "idx_program_term_master_name", columnList = "name"),
      @Index(name = "idx_program_term_master_term_sequence", columnList = "term_sequence"),
      @Index(
          name = "idx_program_term_master_program_term_system_id",
          columnList = "program_term_system_id")
    })
@EntityListeners(ProgramTermMasterListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramTermMaster extends AbstractRBACModel {

  @Column(name = "name", comment = "Name of the a program term master")
  private String name;

  @Column(name = "description", comment = "Description of the program term master")
  private String description;

  @Column(name = "term_sequence", comment = "Sequence of the term in the program term system")
  private Integer termSequence;

  @ManyToOne
  @JoinColumn(
      name = "program_term_system_id",
      foreignKey = @ForeignKey(name = "fk_program_term_system_program_term_master"),
      comment = "Foreign key referencing the program term master")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ProgramTermSystem programTermSystem;
}
