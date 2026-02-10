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
@Table(name = "program_term_master", comment = "Table storing program term master details")
@EntityListeners(ProgramTermMasterListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramTermMaster extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the program term master")
  private String code;

  @Column(name = "name", comment = "Name of the a program term master")
  private String name;

  @Column(name = "description", comment = "Description of the program term master")
  private String description;

  @ManyToOne
  @JoinColumn(
      name = "program_term_master_id",
      foreignKey = @ForeignKey(name = "fk_program_term_system_program_term_master"),
      comment = "Foreign key referencing the program term master")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ProgramTermSystem programTermSystem;
}
