package com.example.lazyco.backend.core.AbstractClasses.Entity;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
public abstract class AbstractDocumentModel implements Serializable, Cloneable {

  @Id private Long id;

  @Version private Long version;

  private Date createdAt;

  private Date updatedAt;

  private String createdBy;

  private String updatedBy;

  @Transient private boolean skipMapping;

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
}
