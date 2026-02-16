package com.example.lazyco.core.AbstractClasses.Entity;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
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
      table = "hibernate_sequences",
      pkColumnName = "sequence_name",
      valueColumnName = "next_hi",
      allocationSize = 1,
      initialValue = 50)
  @Column(name = "id", comment = "Primary key of the table")
  private Long id;

  //  @Version private Long version; // for optimistic locking safety

  @Column(name = "created_at", comment = "Record creation timestamp", updatable = false)
  private Date createdAt;

  @Column(name = "updated_at", comment = "Record last update timestamp")
  private Date updatedAt;

  @Column(name = "created_by", comment = "User who created the record", updatable = false)
  private String createdBy;

  @Column(name = "updated_by", comment = "User who last updated the record")
  private String updatedBy;

  @Transient
  @SuppressWarnings("rawtypes")
  private AbstractDTO dto;

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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    AbstractModel that = (AbstractModel) obj;
    return id != null && id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public String mergeObject(Object... value) {
    StringBuilder merged = new StringBuilder();
    for (Object val : value) {
      if (val != null && !val.toString().trim().isEmpty()) {
        if (!merged.isEmpty()) {
          merged.append(" ");
        }
        merged.append(val.toString().trim());
      }
    }
    return merged.toString();
  }
}
