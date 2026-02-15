package com.example.lazyco.entities.Institution;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AcademicProgram.AcademicProgram;
import com.example.lazyco.entities.University.University;
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
    name = "institution",
    comment = "Table storing institutions for universities.",
    indexes = {
      @Index(name = "idx_institution_code", columnList = "code"),
      @Index(name = "idx_institution_name", columnList = "name"),
      @Index(name = "idx_institution_is_active", columnList = "is_active")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_institution_code", columnNames = "code")})
@EntityListeners(InstitutionListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Institution extends AbstractRBACModel {

  @Column(name = "code", comment = "Code representing the institution")
  private String code;

  @Column(name = "name", comment = "Name of the institution")
  private String name;

  @Column(name = "description", comment = "Description of the institution")
  private String description;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Whether this institution is active")
  private Boolean isActive;

  @ManyToOne
  @JoinColumn(
      name = "university_id",
      foreignKey = @ForeignKey(name = "fk_institution_university"),
      nullable = false,
      comment = "Reference to the university this institution belongs to.")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private University university;

  @OneToMany(mappedBy = "institution")
  private Set<AcademicProgram> academicPrograms;
}
