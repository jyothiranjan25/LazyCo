package com.example.lazyco.backend.core.AbstractClasses.DTO;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.OrderByDTO;
import com.example.lazyco.backend.core.AbstractClasses.Filter.FilterFieldMetadata;
import com.example.lazyco.backend.core.File.FileDTO;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
public abstract class AbstractDTO<D> implements Serializable, Cloneable {

  protected Long id;

  @Expose(serialize = false, deserialize = false)
  private Long version;

  private String ApiAction;

  // List of objects for bulk operations
  private List<D> objects;

  // Pagination fields
  private Integer pageSize;
  private Integer pageOffset;

  @Expose(deserialize = false)
  private Long totalRecords;

  // Audit fields
  @Expose(serialize = false, deserialize = false)
  private String userGroup;

  @Expose(serialize = false, deserialize = false)
  private Date createdAt;

  @Expose(serialize = false, deserialize = false)
  private String createdBy;

  @Expose(serialize = false, deserialize = false)
  private Date updatedAt;

  @Expose(serialize = false, deserialize = false)
  private String updatedBy;

  // This field is used to indicate if the operation should be atomic or not
  private Boolean isAtomicOperation;

  @Expose(serialize = false, deserialize = false)
  private Boolean hasError;

  @Expose(deserialize = false)
  private String message;

  // filtering fields
  @Expose(serialize = false, deserialize = false)
  private List<Long> idsIn;

  @Expose(serialize = false, deserialize = false)
  private List<Long> idsNotIn;

  private List<OrderByDTO> orderBy;

  @Expose(serialize = false, deserialize = false)
  private Map<String, FileDTO> fileMap;

  private Boolean getFilterMetadata;

  private List<FilterFieldMetadata> filterFieldMetadata;

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
