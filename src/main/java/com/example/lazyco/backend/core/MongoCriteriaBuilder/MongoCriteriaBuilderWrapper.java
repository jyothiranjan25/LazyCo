package com.example.lazyco.backend.core.MongoCriteriaBuilder;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CriteriaBuilder.OrderBy;
import java.util.*;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Getter
@Setter
@SuppressWarnings({"rawtypes", "unchecked"})
public class MongoCriteriaBuilderWrapper {

  private Query query;
  private AbstractDTO filter;
  private Criteria finalCriteria;
  private Map<String, String> aliasToPathMap = new HashMap<>();
  private boolean isDistinct = true;

  public MongoCriteriaBuilderWrapper(AbstractDTO filter) {
    this.query = new Query();
    this.filter = filter;
    this.finalCriteria = new Criteria();
  }

  public Query getFinalQuery() {
    if (!finalCriteria.getCriteriaObject().isEmpty()) {
      query.addCriteria(finalCriteria);
    }
    return query;
  }

  private MongoCriteriaBuilderWrapper addCriteria(Criteria criteria) {
    this.finalCriteria = this.finalCriteria.andOperator(criteria);
    return this;
  }

  // -------------------------------
  // Common predicate methods (fluent)
  // -------------------------------
  public void eq(String key, Object value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).is(value);
  }

  public Criteria getEqualPredicate(String key, Object value) {
    return Criteria.where(getFieldPath(key)).is(value);
  }

  public void equalIgnoreCase(String key, Object value) {
    if (value instanceof String) {
      finalCriteria =
          finalCriteria
              .and(getFieldPath(key))
              .regex("^" + Pattern.quote(value.toString()) + "$", "i");
    } else {
      eq(key, value);
    }
  }

  public void notEqual(String key, Object value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).ne(value);
  }

  public void notEqualIgnoreCase(String key, Object value) {
    if (value instanceof String) {
      finalCriteria =
          finalCriteria
              .and(getFieldPath(key))
              .not()
              .regex("^" + Pattern.quote(value.toString()) + "$", "i");
    } else {
      notEqual(key, value);
    }
  }

  public Criteria getNotEqualPredicate(String key, Object value) {
    return Criteria.where(getFieldPath(key)).ne(value);
  }

  public void gt(String key, Object value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).gt(value);
  }

  public Criteria getGtPredicate(String key, Object value) {
    return Criteria.where(getFieldPath(key)).gt(value);
  }

  public void lt(String key, Object value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).lt(value);
  }

  public Criteria getLtPredicate(String key, Object value) {
    return Criteria.where(getFieldPath(key)).lt(value);
  }

  public void ge(String key, Object value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).gte(value);
  }

  public Criteria getGePredicate(String key, Object value) {
    return Criteria.where(getFieldPath(key)).gte(value);
  }

  public void le(String key, Object value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).lte(value);
  }

  public Criteria getLePredicate(String key, Object value) {
    return Criteria.where(getFieldPath(key)).lte(value);
  }

  public void in(String key, List value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).in(value);
  }

  public void in(String key, Collection<?> value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).in(value);
  }

  public Criteria getInPredicate(String key, List value) {
    return Criteria.where(getFieldPath(key)).in(value);
  }

  public Criteria getInPredicate(String key, Collection<?> value) {
    return Criteria.where(getFieldPath(key)).in(value);
  }

  public void notIn(String key, List value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).nin(value);
  }

  public void notIn(String key, Collection<?> value) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).nin(value);
  }

  public Criteria getNotInPredicate(String key, List value) {
    return Criteria.where(getFieldPath(key)).nin(value);
  }

  public Criteria getNotInPredicate(String key, Collection<?> value) {
    return Criteria.where(getFieldPath(key)).nin(value);
  }

  public void like(String key, String value) {
    // Convert SQL LIKE pattern to MongoDB regex
    String regexPattern = value.replace("%", ".*").replace("_", ".");
    finalCriteria = finalCriteria.and(getFieldPath(key)).regex(regexPattern);
  }

  public Criteria getLikePredicate(String key, String value) {
    String regexPattern = value.replace("%", ".*").replace("_", ".");
    return Criteria.where(getFieldPath(key)).regex(regexPattern);
  }

  public void iLike(String key, String value) {
    // Case-insensitive LIKE
    String regexPattern = value.replace("%", ".*").replace("_", ".");
    finalCriteria = finalCriteria.and(getFieldPath(key)).regex(regexPattern, "i");
  }

  public Criteria getILikePredicate(String key, String value) {
    String regexPattern = value.replace("%", ".*").replace("_", ".");
    return Criteria.where(getFieldPath(key)).regex(regexPattern, "i");
  }

  public void isNull(String key) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).isNull();
  }

  public Criteria getIsNullPredicate(String key) {
    return Criteria.where(getFieldPath(key)).isNull();
  }

  public void isNotNull(String key) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).ne(null);
  }

  public Criteria getIsNotNullPredicate(String key) {
    return Criteria.where(getFieldPath(key)).ne(null);
  }

  public void isEmpty(String key) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).size(0);
  }

  public Criteria getIsEmptyPredicate(String key) {
    return Criteria.where(getFieldPath(key)).size(0);
  }

  public void between(String key, Object from, Object to) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).gte(from).lte(to);
  }

  public Criteria getBetweenPredicate(String key, Object from, Object to) {
    return Criteria.where(getFieldPath(key)).gte(from).lte(to);
  }

  public void and(Criteria... criterias) {
    finalCriteria = finalCriteria.andOperator(criterias);
  }

  public Criteria getAndPredicate(Criteria... criterias) {
    return new Criteria().andOperator(criterias);
  }

  public void or(Criteria... criterias) {
    finalCriteria = finalCriteria.orOperator(criterias);
  }

  public Criteria getOrPredicate(Criteria... criterias) {
    return new Criteria().orOperator(criterias);
  }

  // -------------------------------
  // Search criteria (fluent)
  // -------------------------------

  public Criteria getSearchCriteria(String key, String keyWord) {
    return getOrPredicate(
        getLikePredicate(key, keyWord + "%"), getLikePredicate(key, "% " + keyWord + "%"));
  }

  // -------------------------------
  // Query configuration
  // -------------------------------

  public void orderBy(String... fieldPaths) {
    orderBy(OrderBy.ASC, fieldPaths);
  }

  public void orderBy(OrderBy direction, String... fieldPaths) {
    Sort sort = null;
    for (String fieldPath : fieldPaths) {
      Sort.Order order;
      if (OrderBy.ASC.equals(direction)) {
        order = Sort.Order.asc(getFieldPath(fieldPath));
      } else {
        order = Sort.Order.desc(getFieldPath(fieldPath));
      }

      if (sort == null) {
        sort = Sort.by(order);
      } else {
        sort = sort.and(Sort.by(order));
      }
    }
    if (sort != null) {
      query.with(sort);
    }
  }

  public void orderByCriteria(String... fieldPaths) {
    Sort sort = null;

    for (String fieldPath : fieldPaths) {
      // Default direction is ASC
      OrderBy direction = OrderBy.ASC;
      String cleanField = fieldPath;

      // Check if fieldPath contains ':asc' or ':desc'
      if (fieldPath.contains(":")) {
        String[] parts = fieldPath.split(":");
        cleanField = parts[0];
        if (parts.length > 1) {
          try {
            direction = OrderBy.valueOf(parts[1].trim().toUpperCase());
          } catch (Exception ignored) {
          }
        }
      }

      Sort.Order order;
      if (OrderBy.ASC.equals(direction)) {
        order = Sort.Order.asc(getFieldPath(cleanField));
      } else {
        order = Sort.Order.desc(getFieldPath(cleanField));
      }

      if (sort == null) {
        sort = Sort.by(order);
      } else {
        sort = sort.and(Sort.by(order));
      }
    }

    if (sort != null) {
      query.with(sort);
    }
  }

  public void clearOrderBy() {
    query.with(Sort.unsorted());
  }

  public void addProjection(String... fieldPaths) {
    for (String fieldPath : fieldPaths) {
      query.fields().include(getFieldPath(fieldPath));
    }
  }

  public void excludeProjection(String... fieldPaths) {
    for (String fieldPath : fieldPaths) {
      query.fields().exclude(getFieldPath(fieldPath));
    }
  }

  // -------------------------------
  // Pagination
  // -------------------------------

  public void skip(long skip) {
    query.skip(skip);
  }

  public void limit(int limit) {
    query.limit(limit);
  }

  // -------------------------------
  // MongoDB specific methods
  // -------------------------------

  public void regex(String key, String pattern) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).regex(pattern);
  }

  public void regex(String key, String pattern, String options) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).regex(pattern, options);
  }

  public void exists(String key, boolean exists) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).exists(exists);
  }

  public void size(String key, int size) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).size(size);
  }

  public void type(String key, int type) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).type(type);
  }

  public void elemMatch(String key, Criteria criteria) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).elemMatch(criteria);
  }

  public void all(String key, Collection<?> values) {
    finalCriteria = finalCriteria.and(getFieldPath(key)).all(values);
  }

  // -------------------------------
  // Helper methods
  // -------------------------------

  public String getFieldPath(String aliasPath) {
    return aliasToPathMap.getOrDefault(aliasPath, aliasPath);
  }

  // -------------------------------
  // Alias methods (for compatibility)
  // -------------------------------

  public void addAlias(String alias, String fieldPath) {
    aliasToPathMap.put(alias, fieldPath);
  }

  public void removeAlias(String alias) {
    aliasToPathMap.remove(alias);
  }
}
