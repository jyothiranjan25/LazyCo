package com.example.lazyco.entities.TermSystem;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
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
    name = "term_system",
    comment = "Table storing term system details",
    indexes = {
      @Index(name = "idx_term_system_code", columnList = "code"),
      @Index(name = "idx_term_system_name", columnList = "name")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_term_system_code", columnNames = "code")})
@EntityListeners(TermSystemListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TermSystem extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the term system")
  private String code;

  @Column(name = "name", comment = "Name of the term system")
  private String name;

  @Column(name = "description", comment = "Description of the term system")
  private String description;

  @OneToMany(mappedBy = "termSystem")
  private Set<TermMaster> termMasters;
}
