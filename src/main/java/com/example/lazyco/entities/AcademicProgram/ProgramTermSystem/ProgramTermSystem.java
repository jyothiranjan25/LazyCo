package com.example.lazyco.entities.AcademicProgram.ProgramTermSystem;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.AcademicProgram;
import com.example.lazyco.entities.AcademicProgram.ProgramTermMaster.ProgramTermMaster;
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
@Table(name = "program_term_system", comment = "Table storing program term system details")
@EntityListeners(ProgramTermSystemListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramTermSystem extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the academic program term system")
  private String code;

  @Column(name = "name", comment = "Name of the academic program term system")
  private String name;

  @Column(name = "description", comment = "Description of the academic program term system")
  private String description;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Whether this academic program term system is active or not")
  private Boolean isActive;

  @ManyToOne
  @JoinColumn(
      name = "academic_program_id",
      foreignKey = @ForeignKey(name = "fk_program_term_system_academic_program"),
      comment = "Foreign key referencing the academic program")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private AcademicProgram academicProgram;

  @OneToMany(mappedBy = "programTermSystem")
  private Set<ProgramTermMaster> programTermMasters;
}
