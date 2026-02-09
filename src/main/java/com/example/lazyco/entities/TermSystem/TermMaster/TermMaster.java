package com.example.lazyco.entities.TermSystem.TermMaster;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.TermSystem.TermSystem;
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
    name = "term_master",
    comment = "Table storing term master details associated with term systems",
    indexes = {
      @Index(name = "idx_term_master_code", columnList = "code"),
      @Index(name = "idx_term_master_name", columnList = "name"),
      @Index(name = "idx_term_master_term_system_id", columnList = "term_system_id")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_term_master_code", columnNames = "code")})
@EntityListeners(TermMasterListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TermMaster extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the term system")
  private String code;

  @Column(name = "name", comment = "Name of the term system")
  private String name;

  @Column(name = "description", comment = "Description of the term system")
  private String description;

  @ManyToOne
  @JoinColumn(
      name = "term_system_id",
      foreignKey = @ForeignKey(name = "fk_term_master_term_system"),
      comment = "Reference to the term system")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private TermSystem termSystem;
}
