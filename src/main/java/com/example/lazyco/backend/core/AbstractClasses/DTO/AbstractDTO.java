package com.example.lazyco.backend.core.AbstractClasses.DTO;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
public abstract class AbstractDTO<D> implements Serializable, Cloneable {

  protected Long id;

  // List of objects for bulk operations
  private List<D> objectsList;

  // Pagination fields
  private Integer pageSize;
  private Integer pageOffset;

  @Expose(deserialize = false)
  private Long totalRecords;

  // Audit fields
  @Expose(serialize = false,deserialize = false)
  private String userGroup;

  @Expose(serialize = false,deserialize = false)
  private Date createdAt;

  @Expose(serialize = false,deserialize = false)
  private String createdBy;

  @Expose(serialize = false,deserialize = false)
  private Date updatedAt;

  @Expose(serialize = false,deserialize = false)
  private String updatedBy;

  // This field is used to indicate if the operation should be atomic or not
  private Boolean isAtomicOperation;

  // This field is used to indicate if the operation was successful or not
  private String errorMessage;

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
