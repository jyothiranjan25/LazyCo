package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Table(
    name = "enum_display_value",
    comment = "Table storing display values for enums",
    indexes = {
      @Index(name = "idx_enum_display_value_enum_code", columnList = "enum_code"),
      @Index(name = "idx_enum_display_value_category", columnList = "category")
    })
@Audited
@EntityListeners(EnumDisplayValueListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EnumDisplayValue extends AbstractRBACModel {

  @Column(name = "enum_code")
  private String enumCode;

  @Column(name = "display_value")
  private String displayValue;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private EnumCategory category;
}
