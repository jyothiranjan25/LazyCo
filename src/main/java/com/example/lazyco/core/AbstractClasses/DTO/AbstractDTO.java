package com.example.lazyco.core.AbstractClasses.DTO;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.OrderByDTO;
import com.example.lazyco.core.AbstractClasses.Filter.FilterFieldMetadata;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.File.FileDTO;
import com.example.lazyco.core.Logger.ApplicationLogger;
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
  private String userModifiedGroup;

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

  // This field holds a map of file identifiers to their corresponding FileDTOs
  @Expose(serialize = false, deserialize = false)
  private Map<String, FileDTO> fileMap;

  @Expose(serialize = false, deserialize = false)
  private FileDTO file;

  // filtering fields
  @Expose(serialize = false, deserialize = false)
  private List<Long> idsIn;

  @Expose(serialize = false, deserialize = false)
  private List<Long> idsNotIn;

  private List<OrderByDTO> orderBy;

  private Boolean getFilterMetadata;

  private List<FilterFieldMetadata> filterFieldMetadata;

  private String searchString;

  // This field holds the actual entity class that this DTO filters
  // It is set based on the @FilteredEntity annotation
  @Expose(serialize = false, deserialize = false)
  private Class<?> filterableEntityClass;

  // Setter with logic to extract from annotation if present
  public void setFilterableEntityClass(Class<?> filterableEntityClass) {
    try {
      // ✅ Check if annotation is present first
      if (this.getClass().isAnnotationPresent(FilteredEntity.class)) {
        FilteredEntity annotation = this.getClass().getAnnotation(FilteredEntity.class);
        Class<?> currentEntityClass = annotation.type();

        if (currentEntityClass != null) {
          this.filterableEntityClass = currentEntityClass;
        } else {
          throw new ExceptionWrapper(
              "Annotation @FilteredEntity does not define a valid entity type");
        }
      } else {
        this.filterableEntityClass = filterableEntityClass;
      }
    } catch (ExceptionWrapper e) {
      throw e; // Re-throw custom exceptions as is
    } catch (Exception t) {
      ApplicationLogger.error(t);
      throw new ExceptionWrapper("Failed to get filterableEntityClass", t);
    }
  }

  // Getter with logic to extract from annotation if not already set
  public Class<?> getFilterableEntityClass() {
    try {
      // ✅ Check if annotation is present first
      if (this.getClass().isAnnotationPresent(FilteredEntity.class)) {
        FilteredEntity annotation = this.getClass().getAnnotation(FilteredEntity.class);
        Class<?> currentEntityClass = annotation.type();

        if (currentEntityClass != null) {
          this.filterableEntityClass = currentEntityClass;
          return this.filterableEntityClass;
        }
      }
    } catch (ExceptionWrapper e) {
      throw e; // Re-throw custom exceptions as is
    } catch (Exception t) {
      ApplicationLogger.error(t);
      throw new ExceptionWrapper("Failed to get filterableEntityClass", t);
    }
    return this.filterableEntityClass;
  }

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
