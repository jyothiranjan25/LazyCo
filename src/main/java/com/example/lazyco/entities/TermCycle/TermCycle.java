package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicYear.AcademicYear;
import com.example.lazyco.entities.ProgramCycle.ProgramCycle;
import com.example.lazyco.entities.TermSystem.TermMaster.TermMaster;
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
    name = "term_cycle",
    comment = "Table storing term cycle details",
    indexes = {
      @Index(name = "idx_term_cycle_code", columnList = "code"),
      @Index(name = "idx_term_cycle_name", columnList = "name"),
      @Index(name = "idx_term_cycle_academic_year_id", columnList = "academic_year_id"),
      @Index(name = "idx_term_cycle_term_master_id", columnList = "term_master_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_term_cycle_code", columnNames = "code"),
      @UniqueConstraint(
          name = "uk_term_cycle_academic_year_term_master",
          columnNames = {"academic_year_id", "term_master_id"})
    })
@EntityListeners(TermCycleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TermCycle extends AbstractRBACModel {

  @Column(name = "code", comment = "Code of the term cycle")
  private String code;

  @Column(name = "name", comment = "Name of the term cycle")
  private String name;

  @Column(name = "description", comment = "Description of the term cycle")
  private String description;

  @ManyToOne
  @JoinColumn(
      name = "academic_year_id",
      foreignKey = @ForeignKey(name = "fk_term_cycle_academic_year"),
      comment = "Foreign key referencing the academic year")
  private AcademicYear academicYear;

  @ManyToOne
  @JoinColumn(
      name = "term_master_id",
      foreignKey = @ForeignKey(name = "fk_term_cycle_term_master"),
      comment = "Foreign key referencing the term master")
  private TermMaster termMaster;

  @OneToMany(mappedBy = "termCycle")
  private Set<ProgramCycle> programCycles;
}
