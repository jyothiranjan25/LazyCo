package com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.ComparisionPredicates.ComparisonPredicates;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.DateUtils.DateRangeDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

@Getter
@Setter
@SuppressWarnings({"rawtypes", "unchecked"})
public class CriteriaBuilderWrapper {

  private Root root;
  private CriteriaQuery query;
  private HibernateCriteriaBuilder criteriaBuilder;
  private AbstractDTO filter;
  private Predicate finalPredicate;

  // Use concurrent maps for thread safety
  private Map<String, Join> joinMap = new ConcurrentHashMap<>();
  private Map<String, Join> fetchMap = new ConcurrentHashMap<>();
  private Map<String, String> aliasToFullyQualifiedPathMap = new ConcurrentHashMap<>();
  private Map<String, Join> fullyQualifiedPathToJoinMap = new ConcurrentHashMap<>();
  private Map<String, JoinType> fullyQualifiedPathToJoinTypeMap = new ConcurrentHashMap<>();
  private Map<Order, Order> orderMap = new ConcurrentHashMap<>();

  private boolean isDistinct = false;

  public CriteriaBuilderWrapper(
      Root root,
      CriteriaQuery query,
      HibernateCriteriaBuilder criteriaBuilder,
      AbstractDTO filter) {
    this.root = root;
    this.query = query;
    this.criteriaBuilder = criteriaBuilder;
    this.filter = filter;
    this.finalPredicate = criteriaBuilder.conjunction();
  }

  public Predicate getFinalPredicate() {
    query.where(finalPredicate);
    return query.getRestriction();
  }

  private CriteriaBuilderWrapper addAndPredicate(Predicate predicate) {
    this.finalPredicate = criteriaBuilder.and(finalPredicate, predicate);
    return this;
  }

  private CriteriaBuilderWrapper addOrPredicate(Predicate predicate) {
    this.finalPredicate = criteriaBuilder.or(finalPredicate, predicate);
    return this;
  }

  // -------------------------------
  // Common predicate methods (fluent)
  // -------------------------------

  public void eq(String key, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getEqualPredicate(key, value));
    }
  }

  public void eq(Path<?> path, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getEqualPredicate(path, value));
    }
  }

  public Predicate getEqualPredicate(String key, Object value) {
    return criteriaBuilder.equal(getExpression(key), value);
  }

  public Predicate getEqualPredicate(Path<?> path, Object value) {
    return criteriaBuilder.equal(path, value);
  }

  public void propertyEq(String column1, String column2) {
    finalPredicate =
        criteriaBuilder.and(
            finalPredicate, criteriaBuilder.equal(getExpression(column1), getExpression(column2)));
  }

  public void equalIgnoreCase(String key, Object value) {
    if (Objects.nonNull(value)) {
      Expression lower = criteriaBuilder.lower(getExpression(key));
      finalPredicate =
          criteriaBuilder.and(
              finalPredicate, criteriaBuilder.equal(lower, value.toString().toLowerCase()));
    }
  }

  public void notEqual(String key, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotEqualPredicate(key, value));
    }
  }

  public void notEqual(Path<?> path, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotEqualPredicate(path, value));
    }
  }

  public void notEqualProperty(String column1, String column2) {
    finalPredicate =
        criteriaBuilder.and(
            finalPredicate,
            criteriaBuilder.notEqual(getExpression(column1), getExpression(column2)));
  }

  public void notEqualIgnoreCase(String key, Object value) {
    if (Objects.nonNull(value)) {
      Expression lower = criteriaBuilder.lower(getExpression(key));
      finalPredicate =
          criteriaBuilder.and(
              finalPredicate, criteriaBuilder.notEqual(lower, value.toString().toLowerCase()));
    }
  }

  public Predicate getNotEqualPredicate(String key, Object value) {
    return getNotEqualPredicate(getExpression(key), value);
  }

  public Predicate getNotEqualPredicate(Path<?> path, Object value) {
    return criteriaBuilder.notEqual(path, value);
  }

  public void gt(String key, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getGtPredicate(key, value));
    }
  }

  public void greaterThan(String column1, String column2) {
    finalPredicate =
        criteriaBuilder.and(
            finalPredicate, getGtPredicate(getExpression(column1), getExpression(column2)));
  }

  public Predicate getGtPredicate(String key, Object value) {
    return getGtPredicate(getExpression(key), value);
  }

  public Predicate getGtPredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getGtPredicate(criteriaBuilder, path, value);
  }

  public Predicate getGtPredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getGtPredicate(criteriaBuilder, path, path2);
  }

  public void lt(String key, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLtPredicate(key, value));
    }
  }

  public void lessThan(String column1, String column2) {
    finalPredicate =
        criteriaBuilder.and(
            finalPredicate, getLtPredicate(getExpression(column1), getExpression(column2)));
  }

  public Predicate getLtPredicate(String key, Object value) {
    return getLtPredicate(getExpression(key), value);
  }

  public Predicate getLtPredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getLtPredicate(criteriaBuilder, path, value);
  }

  public Predicate getLtPredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getLtPredicate(criteriaBuilder, path, path2);
  }

  public void ge(String key, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getGePredicate(key, value));
    }
  }

  public void greaterThenOrEqual(String column1, String column2) {
    Predicate gePredicate = getGePredicate(getExpression(column1), getExpression(column2));
    finalPredicate = criteriaBuilder.and(finalPredicate, gePredicate);
  }

  public Predicate greaterThanOrEqual(String key, Object value) {
    Predicate gePredicate = getGePredicate(getExpression(key), criteriaBuilder.literal(value));
    return criteriaBuilder.and(finalPredicate, gePredicate);
  }

  public Predicate greaterThanOrEqual(Path<?> path, Object value) {
    Predicate gePredicate = getGePredicate(path, criteriaBuilder.literal(value));
    return criteriaBuilder.and(finalPredicate, gePredicate);
  }

  public Predicate getGePredicate(String key, Object value) {
    return getGePredicate(getExpression(key), value);
  }

  public Predicate getGePredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getGePredicate(criteriaBuilder, path, value);
  }

  public Predicate getGePredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getGePredicate(criteriaBuilder, path, path2);
  }

  public void le(String key, Object value) {
    if (Objects.nonNull(value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLePredicate(key, value));
    }
  }

  public void lessThenOrEqual(String column1, String column2) {
    Predicate lePredicate = getLePredicate(getExpression(column1), getExpression(column2));
    finalPredicate = criteriaBuilder.and(finalPredicate, lePredicate);
  }

  public Predicate lessThanOrEqual(String key, Object value) {
    Predicate lePredicate = getLePredicate(getExpression(key), criteriaBuilder.literal(value));
    return criteriaBuilder.and(finalPredicate, lePredicate);
  }

  public Predicate lessThanOrEqual(Path<?> path, Object value) {
    Predicate lePredicate = getLePredicate(path, criteriaBuilder.literal(value));
    return criteriaBuilder.and(finalPredicate, lePredicate);
  }

  public Predicate getLePredicate(String key, Object value) {
    return getLePredicate(getExpression(key), value);
  }

  public Predicate getLePredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getLePredicate(criteriaBuilder, path, value);
  }

  public Predicate getLePredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getLePredicate(criteriaBuilder, path, path2);
  }

  public void in(String key, List value) {
    if (value != null && !value.isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getInPredicate(key, value));
    }
  }

  public void in(Path<?> path, Collection<?> value) {
    if (value != null && !value.isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getInPredicate(path, value));
    }
  }

  public Predicate getInPredicate(String key, List value) {
    return getInPredicate(getExpression(key), value);
  }

  public Predicate getInPredicate(Path<?> path, Collection<?> value) {
    return path.in(value);
  }

  public void notIn(String key, List value) {
    if (value != null && !value.isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotInPredicate(key, value));
    }
  }

  public void notIn(Path<?> path, Collection<?> value) {
    if (value != null && !value.isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotInPredicate(path, value));
    }
  }

  public Predicate getNotInPredicate(String key, List value) {
    return getNotInPredicate(getExpression(key), value);
  }

  public Predicate getNotInPredicate(Path<?> path, Collection<?> value) {
    return path.in(value).not();
  }

  public void like(String key, String value) {
    if (value != null && !value.trim().isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLikePredicate(key, value));
    }
  }

  public void like(Path<?> path, String value) {
    if (value != null && !value.trim().isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLikePredicate(path, value));
    }
  }

  public Predicate getLikePredicate(String key, String value) {
    return getLikePredicate(getExpression(key), value);
  }

  public Predicate getLikePredicate(Path<?> path, String value) {
    return criteriaBuilder.like((Expression<String>) path, value);
  }

  public void iLike(String key, String value) {
    if (value != null && !value.trim().isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getILikePredicate(key, value));
    }
  }

  public void iLike(Path<?> path, String value) {
    if (value != null && !value.trim().isEmpty()) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getILikePredicate(path, value));
    }
  }

  public Predicate getILikePredicate(String key, String value) {
    return getILikePredicate(getExpression(key), value);
  }

  public Predicate getILikePredicate(Path<?> path, String value) {
    return criteriaBuilder.ilike((Expression<String>) path, value);
  }

  public Predicate getILikePredicate(Path<?> path, Object value) {
    return criteriaBuilder.ilike((Expression<String>) path, (Expression<String>) value);
  }

  public Predicate getNotILikePredicate(String key, Object value) {
    return getNotILikePredicate(getExpression(key), value);
  }

  public Predicate getNotILikePredicate(Path<?> path, Object value) {
    return criteriaBuilder.not(
        criteriaBuilder.ilike((Expression<String>) path, (Expression<String>) value));
  }

  public void isNull(String key) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsNullPredicate(key));
  }

  public void isNull(Path<?> path) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsNullPredicate(path));
  }

  public Predicate getIsNullPredicate(String key) {
    return getIsNullPredicate(getExpression(key));
  }

  public Predicate getIsNullPredicate(Path<?> key) {
    return criteriaBuilder.isNull(key);
  }

  public void isNotNull(String key) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsNotNullPredicate(key));
  }

  public void isNotNull(Path<?> path) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsNotNullPredicate(path));
  }

  public Predicate getIsNotNullPredicate(String key) {
    return getIsNotNullPredicate(getExpression(key));
  }

  public Predicate getIsNotNullPredicate(Path<?> key) {
    return criteriaBuilder.isNotNull(key);
  }

  public void isEmpty(String id) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsEmptyPredicate(id));
  }

  private Expression<Boolean> getIsEmptyPredicate(String key) {
    return criteriaBuilder.isEmpty(root.get(key));
  }

  public void between(String key, Object from, Object to) {
    if (Objects.nonNull(from) && Objects.nonNull(to)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getBetweenPredicate(key, from, to));
    }
  }

  public void between(Path<?> key, Object from, Object to) {
    if (Objects.nonNull(from) && Objects.nonNull(to)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getBetweenPredicate(key, from, to));
    }
  }

  public Predicate getBetweenPredicate(String key, Object from, Object to) {
    return getBetweenPredicate(getExpression(key), from, to);
  }

  public Predicate getBetweenPredicate(Path<?> path, Object from, Object to) {
    return ComparisonPredicates.factory(from).getBetweenPredicate(criteriaBuilder, path, from, to);
  }

  public void and(Predicate... predicates) {
    if (predicates != null && predicates.length > 0) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getAndPredicate(predicates));
    }
  }

  public Predicate getAndPredicate(Predicate... predicates) {
    return criteriaBuilder.and(predicates);
  }

  public void or(Predicate... predicates) {
    if (predicates != null && predicates.length > 0) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getOrPredicate(predicates));
    }
  }

  public Predicate getOrPredicate(Predicate... predicates) {
    return criteriaBuilder.or(predicates);
  }

  // -------------------------------
  // Search criteria (fluent)
  // -------------------------------

  public Predicate getSearchCriteria(String key, String keyWord) {
    return getSearchCriteria(getExpression(key), keyWord);
  }

  public Predicate getSearchCriteria(Path<?> path, String keyWord) {
    if (keyWord == null || keyWord.trim().isEmpty()) {
      return criteriaBuilder.conjunction(); // Return true predicate for empty search
    }
    return getOrPredicate(getSearchPredicate(path, "% " + keyWord + "%"));
  }

  public Predicate getSearchPredicate(Path<?> path, String value) {
    return criteriaBuilder.ilike(
        criteriaBuilder.lower(criteriaBuilder.toString((Expression<Character>) path)),
        value.toLowerCase());
  }

  // -------------------------------
  // Query configuration - Improved performance
  // -------------------------------

  public void setDistinct() {
    query.select(root).distinct(isDistinct);
  }

  public void groupBy(String... fieldPaths) {
    if (fieldPaths != null && fieldPaths.length > 0) {
      List<Expression<?>> expressions = new ArrayList<>();
      for (String fieldPath : fieldPaths) {
        try {
          expressions.add(getExpression(fieldPath));
        } catch (Exception e) {
          ApplicationLogger.error("Failed to add groupBy field: " + fieldPath, e);
        }
      }
      if (!expressions.isEmpty()) {
        query.groupBy(expressions);
      }
    }
  }

  public void orderBy() {
    List<Order> orders = new ArrayList<>(orderMap.keySet());
    if (!orders.isEmpty()) query.orderBy(orders);
    else query.orderBy();
  }

  public void clearOrderBy() {
    orderMap.clear();
  }

  public void orderBy(String key) {
    orderBy(key, OrderType.ASC);
  }

  public void orderBy(String key, OrderType orderType) {
    // Ignore null or empty keys
    if (key == null || key.trim().isEmpty()) {
      return;
    }
    // Default to ASC if orderType is null
    if (orderType == null) {
      orderType = OrderType.ASC;
    }

    try {
      Order order = getOrder(getExpression(key), orderType);
      orderMap.putIfAbsent(order, order);
    } catch (Exception e) {
      ApplicationLogger.error("Failed to order by: " + key, e);
    }
  }

  private Order getOrder(Path<?> path, OrderType orderType) {
    if (orderType.equals(OrderType.ASC)) {
      return criteriaBuilder.asc(path);
    } else {
      return criteriaBuilder.desc(path);
    }
  }

  public void orderBy(List<Order> orders) {
    if (orders != null && !orders.isEmpty()) {
      for (Order order : orders) {
        orderMap.putIfAbsent(order, order);
      }
    }
  }

  // use format: "fieldPath:asc" or "fieldPath:desc"
  public List<Order> orderByCriteria(String... fieldPaths) {
    if (fieldPaths != null && fieldPaths.length > 0) {
      List<Order> orders = new ArrayList<>();

      for (String fieldPath : fieldPaths) {
        try {
          // Default direction is ASC
          OrderType direction = OrderType.ASC;
          String cleanField = fieldPath;

          // Check if fieldPath contains ':asc' or ':desc'
          if (fieldPath.contains(":")) {
            String[] parts = fieldPath.split(":");
            cleanField = parts[0];
            if (parts.length > 1) {
              try {
                direction = OrderType.valueOf(parts[1].trim().toUpperCase());
              } catch (IllegalArgumentException ignored) {
                // Keep default ASC if invalid direction
              }
            }
          }

          Path<?> expression = getExpression(cleanField);
          Order order = getOrder(expression, direction);
          orders.add(order);
        } catch (Exception e) {
          ApplicationLogger.error("Failed to add order by criteria field: " + fieldPath, e);
        }
      }
      return orders;
    }
    return Collections.emptyList();
  }

  // -------------------------------
  // Projection methods - Improved error handling
  // -------------------------------

  public void addProjection(String... fieldPaths) {
    if (fieldPaths != null && fieldPaths.length > 0) {
      List<Expression<?>> expressions = new ArrayList<>();
      for (String fieldPath : fieldPaths) {
        try {
          expressions.add(getExpression(fieldPath));
        } catch (Exception e) {
          ApplicationLogger.error("Failed to add projection field: " + fieldPath, e);
        }
      }
      if (!expressions.isEmpty()) {
        query.multiselect(expressions);
      }
    }
  }

  // -------------------------------
  // Helper methods - Improved error handling
  // -------------------------------

  public Path getExpression(String aliasPath) {
    try {
      String fullyQualifiedPath = getFullyQualifiedPath(aliasPath);
      return FieldFilterUtils.getPathNode(this, fullyQualifiedPath);
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get expression for: " + aliasPath, e);
      throw new RuntimeException("Failed to resolve path: " + aliasPath, e);
    }
  }

  public String getFullyQualifiedPath(String aliasPath) {
    if (aliasPath == null || aliasPath.trim().isEmpty()) {
      throw new IllegalArgumentException("Alias path cannot be null or empty");
    }

    int propertyIndex = aliasPath.lastIndexOf('.') + 1;
    String property = aliasPath.substring(propertyIndex);
    String fullyQualifiedPath;

    if (propertyIndex == 0) {
      fullyQualifiedPath = property;
    } else {
      String parentAlias = aliasPath.substring(0, propertyIndex - 1);
      String parentPath = aliasToFullyQualifiedPathMap.get(parentAlias);
      if (parentPath == null) {
        throw new IllegalArgumentException("No mapping found for alias: " + parentAlias);
      }
      fullyQualifiedPath = parentPath + "." + property;
    }
    return fullyQualifiedPath;
  }

  // -------------------------------
  // Join methods - Improved with error handling and deduplication
  // -------------------------------

  public void join(String property) {
    join(property, property);
  }

  public void join(String property, String alias) {
    join(property, alias, JoinType.LEFT);
  }

  public void join(String property, JoinType joinType) {
    join(property, property, joinType);
  }

  public void join(String property, String alias, JoinType joinType) {
    if (property == null || property.trim().isEmpty()) {
      throw new IllegalArgumentException("Property path cannot be null or empty");
    }

    try {
      String[] props = property.split("\\.");
      From<?, ?> from = root;
      StringBuilder fqPath = new StringBuilder();

      for (int i = 0; i < props.length; i++) {
        String prop = props[i];
        fqPath.append(prop);
        String fqPathStr = fqPath.toString();

        // Check if join already exists to avoid duplicates
        Join join = joinMap.get(fqPathStr);
        if (join == null) {
          join = from.join(prop, joinType);
          joinMap.put(fqPathStr, join);
          fullyQualifiedPathToJoinMap.put(fqPathStr, join);
          fullyQualifiedPathToJoinTypeMap.put(fqPathStr, joinType);
        }
        from = join;

        if (i < props.length - 1) {
          fqPath.append(".");
        }
      }

      aliasToFullyQualifiedPathMap.put(alias, fqPath.toString());
    } catch (Exception e) {
      ApplicationLogger.error("Failed to create join for property: " + property, e);
      throw new RuntimeException("Failed to create join: " + property, e);
    }
  }

  // -------------------------------
  // Join Fetch methods - Improved
  // -------------------------------

  public void fetch(String fetchProperty) {
    fetch(fetchProperty, fetchProperty);
  }

  public void fetch(String fetchProperty, String alias) {
    joinFetch(fetchProperty, alias, JoinType.LEFT);
  }

  public void fetch(String fetchProperty, JoinType joinType) {
    joinFetch(fetchProperty, fetchProperty, joinType);
  }

  public void joinFetch(String property, String alias, JoinType joinType) {
    if (property == null || property.trim().isEmpty()) {
      throw new IllegalArgumentException("Property path cannot be null or empty");
    }

    try {
      String[] props = property.split("\\.");
      From<?, ?> from = root;
      StringBuilder fqPath = new StringBuilder();

      for (int i = 0; i < props.length; i++) {
        String prop = props[i];
        fqPath.append(prop);
        String fqPathStr = fqPath.toString();

        // Check if fetch already exists
        if (!fetchMap.containsKey(fqPathStr)) {
          Fetch fetch = from.fetch(prop, joinType);
          Join join = (Join) fetch;
          joinMap.put(fqPathStr, join);
          fetchMap.put(fqPathStr, join);
          fullyQualifiedPathToJoinMap.put(fqPathStr, join);
          fullyQualifiedPathToJoinTypeMap.put(fqPathStr, joinType);
        }

        from = joinMap.get(fqPathStr);

        if (i < props.length - 1) {
          fqPath.append(".");
        }
      }

      aliasToFullyQualifiedPathMap.put(alias, fqPath.toString());
    } catch (Exception e) {
      ApplicationLogger.error("Failed to create fetch for property: " + property, e);
      throw new RuntimeException("Failed to create fetch: " + property, e);
    }
  }

  public void addDateTimeRangeConflictCriteria(
      DateRangeDTO dateRangeDTO, String startAlias, String endAlias) {
    if (dateRangeDTO == null || startAlias == null || endAlias == null) {
      return;
    }

    try {
      Disjunction d = new Disjunction(this);

      Conjunction startDateBetween = new Conjunction(this);
      startDateBetween.add(this.getGtPredicate(startAlias, dateRangeDTO.getStart()));
      startDateBetween.add(this.getLtPredicate(startAlias, dateRangeDTO.getEnd()));

      Conjunction endDateBetween = new Conjunction(this);
      endDateBetween.add(this.getGtPredicate(endAlias, dateRangeDTO.getStart()));
      endDateBetween.add(this.getLtPredicate(endAlias, dateRangeDTO.getEnd()));

      Conjunction startAndEnd = new Conjunction(this);
      startAndEnd.add(this.getLePredicate(startAlias, dateRangeDTO.getStart()));
      startAndEnd.add(this.getGePredicate(endAlias, dateRangeDTO.getEnd()));

      d.add(startDateBetween.build());
      d.add(endDateBetween.build());
      d.add(startAndEnd.build());
      this.and(d.build());
    } catch (Exception e) {
      ApplicationLogger.error("Failed to add date range conflict criteria", e);
    }
  }
}
