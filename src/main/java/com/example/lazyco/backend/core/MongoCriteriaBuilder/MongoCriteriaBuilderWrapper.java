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
  private List<Criteria> criteriaList = new ArrayList<>();
  private Map<String, String> aliasToPathMap = new HashMap<>();
  private boolean isDistinct = true;

  public MongoCriteriaBuilderWrapper(AbstractDTO filter) {
    this.query = new Query();
    this.filter = filter;
  }

  public Query getFinalQuery() {
    if (!criteriaList.isEmpty()) {
      Criteria combined = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
      query.addCriteria(combined);
    }
    return query;
  }

  private void addCriteria(Criteria criteria) {
    this.criteriaList.add(criteria);
  }

  private Criteria getCriteria(String key) {
    return Criteria.where(getFieldPath(key));
  }

  // -------------------------------
  // Common predicate methods (fluent)
  // -------------------------------
  public void eq(String key, Object value) {
    addCriteria(getCriteria(key).is(value));
  }

  public void equalIgnoreCase(String key, Object value) {
    if (value instanceof String) {
      addCriteria(getCriteria(key).regex("^" + Pattern.quote(value.toString()) + "$", "i"));
    } else {
      eq(key, value);
    }
  }

  public void notEqual(String key, Object value) {
    addCriteria(getCriteria(key).ne(value));
  }

  public void notEqualIgnoreCase(String key, Object value) {
    if (value instanceof String) {
      addCriteria(getCriteria(key).not().regex("^" + Pattern.quote(value.toString()) + "$", "i"));
    } else {
      notEqual(key, value);
    }
  }

  public void gt(String key, Object value) {
    addCriteria(getCriteria(key).gt(value));
  }

  public void lt(String key, Object value) {
    addCriteria(getCriteria(key).lt(value));
  }

  public void ge(String key, Object value) {
    addCriteria(getCriteria(key).gte(value));
  }

  public void le(String key, Object value) {
    addCriteria(getCriteria(key).lte(value));
  }

  public void in(String key, List value) {
    addCriteria(getCriteria(key).in(value));
  }

  public void in(String key, Collection<?> value) {
    addCriteria(getCriteria(key).in(value));
  }

  public void notIn(String key, List value) {
    addCriteria(getCriteria(key).nin(value));
  }

  public void notIn(String key, Collection<?> value) {
    addCriteria(getCriteria(key).nin(value));
  }

  public void like(String key, String value) {
    // Convert SQL LIKE pattern to MongoDB regex
    String regexPattern = value.replace("%", ".*").replace("_", ".");
    addCriteria(getCriteria(key).regex(regexPattern));
  }

  public void iLike(String key, String value) {
    String regexPattern = value.replace("%", ".*").replace("_", ".");
    addCriteria(getCriteria(key).regex(regexPattern, "i"));
  }

  public void isNull(String key) {
    addCriteria(getCriteria(key).is(null));
  }

  public void isNotNull(String key) {
    addCriteria(getCriteria(key).ne(null));
  }

  public void isEmpty(String key) {
    addCriteria(getCriteria(key).size(0));
  }

  public void between(String key, Object from, Object to) {
    addCriteria(getCriteria(key).gte(from).lte(to));
  }

  public void and(Criteria... criteria) {
    addCriteria(new Criteria().andOperator(criteria));
  }

  public Criteria getAndPredicate(Criteria... criteria) {
    return new Criteria().andOperator(criteria);
  }

  public void or(Criteria... criteria) {
    addCriteria(new Criteria().orOperator(criteria));
  }

  public Criteria getOrPredicate(Criteria... criteria) {
   return new Criteria().orOperator(criteria);
  }

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
  // MongoDB specific methods
  // -------------------------------

  public void regex(String key, String pattern) {
    addCriteria(getCriteria(key).regex(pattern));
  }

  public void regex(String key, String pattern, String options) {
    addCriteria(getCriteria(key).regex(pattern, options));
  }

  public void exists(String key, boolean exists) {
    addCriteria(getCriteria(key).exists(exists));
  }

  public void size(String key, int size) {
    addCriteria(getCriteria(key).size(size));
  }

  public void type(String key, int type) {
    addCriteria(getCriteria(key).type(type));
  }

  public void elemMatch(String key, Criteria criteria) {
    addCriteria(getCriteria(key).elemMatch(criteria));
  }

  public void all(String key, Collection<?> values) {
    addCriteria(getCriteria(key).all(values));
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
