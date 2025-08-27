package com.example.lazyco.backend.core.AbstractClasses.Entity;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.schema.database.AbstractBaseSchema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AbstractModelListener.class)
public abstract class AbstractModel implements Serializable, Cloneable {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "hilo_generator")
  @TableGenerator(
      name = "hilo_generator",
      table = AbstractBaseSchema.HIBERNATE_SEQUENCES,
      pkColumnName = "sequence_name",
      valueColumnName = "next_hi",
      allocationSize = 1)
  @Column(name = AbstractBaseSchema.ID)
  private Long id;

  @Version private Long version; // for optimistic locking safety

  @Column(name = AbstractBaseSchema.CREATED_AT)
  private Date createdAt;

  @Column(name = AbstractBaseSchema.UPDATED_AT)
  private Date updatedAt;

  @Column(name = AbstractBaseSchema.CREATED_BY)
  private String createdBy;

  @Column(name = AbstractBaseSchema.UPDATED_BY)
  private String updatedBy;

  @Transient private boolean skipMapping;

  @Transient private AbstractDTO dto;

  @Override
  public Object clone() {
    Object o1 = null;
    try {
      o1 = super.clone();
      // SerializationUtils.clone performs a deep clone
      return SerializationUtils.clone(this);
    } catch (Exception e) {
      return o1;
    }
  }
}
