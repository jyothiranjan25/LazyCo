package com.example.lazyco.core.AbstractClasses.CriteriaBuilder;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.ComparisionPredicates.ComparisonPredicates;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.DateUtils.DateRangeDTO;
import com.example.lazyco.core.Logger.ApplicationLogger;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.PluralAttribute;
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

  public void setDistinct() {
    query.select(root).distinct(isDistinct);
  }

  public void getFinalPredicate() {
    query.where(finalPredicate);
    query.getRestriction();
  }

  // -------------------------------
  // Common predicate methods (fluent)
  // -------------------------------

  /** Equal predicates */
  public void eq(String key, Object value) {
    eq(getExpression(key), value);
  }

  public void eq(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getEqualPredicate(path, value));
    }
  }

  public Predicate getEqualPredicate(String key, Object value) {
    return getEqualPredicate(getExpression(key), value);
  }

  public Predicate getEqualPredicate(Path<?> path, Object value) {
    return criteriaBuilder.equal(path, value);
  }

  public void equalIgnoreCase(String key, Object value) {
    equalIgnoreCase(getExpression(key), value);
  }

  public void equalIgnoreCase(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      Expression lowerColumn = criteriaBuilder.lower((Expression<String>) path);
      String lowerValue = value.toString().toLowerCase();
      finalPredicate =
          criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(lowerColumn, lowerValue));
    }
  }

  public void propertyEq(String column1, String column2) {
    Path<?> column1Path = getExpression(column1);
    Path<?> column2Path = getExpression(column2);
    finalPredicate =
        criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(column1Path, column2Path));
  }

  /** Not equal predicates */
  public void notEq(String key, Object value) {
    if (Objects.nonNull(value)) {
      notEq(getExpression(key), value);
    }
  }

  public void notEq(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotEqualPredicate(path, value));
    }
  }

  public Predicate getNotEqualPredicate(String key, Object value) {
    return getNotEqualPredicate(getExpression(key), value);
  }

  public Predicate getNotEqualPredicate(Path<?> path, Object value) {
    return criteriaBuilder.notEqual(path, value);
  }

  public void notEqualIgnoreCase(String key, Object value) {
    notEqualIgnoreCase(getExpression(key), value);
  }

  public void notEqualIgnoreCase(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      Expression lowerColumn = criteriaBuilder.lower((Expression<String>) path);
      String lowerValue = value.toString().toLowerCase();
      finalPredicate =
          criteriaBuilder.and(finalPredicate, criteriaBuilder.notEqual(lowerColumn, lowerValue));
    }
  }

  public void notPropertyEq(String column1, String column2) {
    Path<?> column1Path = getExpression(column1);
    Path<?> column2Path = getExpression(column2);
    finalPredicate =
        criteriaBuilder.and(finalPredicate, criteriaBuilder.notEqual(column1Path, column2Path));
  }

  /** Greater than predicates */
  public void gt(String key, Object value) {
    gt(getExpression(key), value);
  }

  public void gt(Path<?> key, Object value) {
    if (validateValue(key, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getGtPredicate(key, value));
    }
  }

  public Predicate getGtPredicate(String key, Object value) {
    return getGtPredicate(getExpression(key), value);
  }

  public Predicate getGtPredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getGtPredicate(criteriaBuilder, path, value);
  }

  public void propertyGt(String column1, String column2) {
    Path<?> column1Path = getExpression(column1);
    Path<?> column2Path = getExpression(column2);
    finalPredicate = criteriaBuilder.and(finalPredicate, getGtPredicate(column1Path, column2Path));
  }

  public Predicate getGtPredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getGtPredicate(criteriaBuilder, path, path2);
  }

  /** Less than predicates */
  public void lt(String key, Object value) {
    lt(getExpression(key), value);
  }

  public void lt(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLtPredicate(path, value));
    }
  }

  public Predicate getLtPredicate(String key, Object value) {
    return getLtPredicate(getExpression(key), value);
  }

  public Predicate getLtPredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getLtPredicate(criteriaBuilder, path, value);
  }

  public void propertyLt(String column1, String column2) {
    Path<?> column1Path = getExpression(column1);
    Path<?> column2Path = getExpression(column2);
    finalPredicate = criteriaBuilder.and(finalPredicate, getLtPredicate(column1Path, column2Path));
  }

  public Predicate getLtPredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getLtPredicate(criteriaBuilder, path, path2);
  }

  /** Greater than or equal predicates */
  public void ge(String key, Object value) {
    ge(getExpression(key), value);
  }

  public void ge(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getGePredicate(path, value));
    }
  }

  public Predicate getGePredicate(String key, Object value) {
    return getGePredicate(getExpression(key), value);
  }

  public Predicate getGePredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getGePredicate(criteriaBuilder, path, value);
  }

  public void propertyGe(String column1, String column2) {
    Path<?> column1Path = getExpression(column1);
    Path<?> column2Path = getExpression(column2);
    finalPredicate = criteriaBuilder.and(finalPredicate, getGePredicate(column1Path, column2Path));
  }

  public Predicate getGePredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getGePredicate(criteriaBuilder, path, path2);
  }

  /** Less than or equal predicates */
  public void le(String key, Object value) {
    le(getExpression(key), value);
  }

  public void le(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLePredicate(path, value));
    }
  }

  public Predicate getLePredicate(String key, Object value) {
    return getLePredicate(getExpression(key), value);
  }

  public Predicate getLePredicate(Path<?> path, Object value) {
    return ComparisonPredicates.factory(value).getLePredicate(criteriaBuilder, path, value);
  }

  public void propertyLe(String column1, String column2) {
    Path<?> column1Path = getExpression(column1);
    Path<?> column2Path = getExpression(column2);
    finalPredicate = criteriaBuilder.and(finalPredicate, getLePredicate(column1Path, column2Path));
  }

  public Predicate getLePredicate(Path<?> path, Path<?> path2) {
    return ComparisonPredicates.factory(path).getLePredicate(criteriaBuilder, path, path2);
  }

  /** IN predicates */
  public void in(String key, List value) {
    in(getExpression(key), value);
  }

  public void in(Path<?> path, Collection<?> value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getInPredicate(path, value));
    }
  }

  public Predicate getInPredicate(String key, List value) {
    return getInPredicate(getExpression(key), value);
  }

  public Predicate getInPredicate(Path<?> path, Collection<?> value) {
    return path.in(value);
  }

  /** NOT IN predicates */
  public void notIn(String key, List value) {
    notIn(getExpression(key), value);
  }

  public void notIn(Path<?> path, Collection<?> value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotInPredicate(path, value));
    }
  }

  public Predicate getNotInPredicate(String key, List value) {
    return getNotInPredicate(getExpression(key), value);
  }

  public Predicate getNotInPredicate(Path<?> path, Collection<?> value) {
    return path.in(value).not();
  }

  /** LIKE predicates */
  public void like(String key, String value) {
    like(getExpression(key), value);
  }

  public void like(Path<?> path, String value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLikePredicate(path, value));
    }
  }

  public void like(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getLikePredicate(path, value));
    }
  }

  public Predicate getLikePredicate(String key, String value) {
    return getLikePredicate(getExpression(key), value);
  }

  public Predicate getLikePredicate(Path<?> path, String value) {
    Expression<String> expressionPath = (Expression<String>) path;
    return criteriaBuilder.like(expressionPath, value);
  }

  public Predicate getLikePredicate(Path<?> path, Object value) {
    Expression<String> expressionPath = (Expression<String>) path;
    Expression<String> expressionValue = criteriaBuilder.literal(value.toString());
    return criteriaBuilder.like(expressionPath, expressionValue);
  }

  /** NOT LIKE predicates */
  public void notLike(String key, String value) {
    notLike(getExpression(key), value);
  }

  public void notLike(Path<?> path, String value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotLikePredicate(path, value));
    }
  }

  public void notLike(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotLikePredicate(path, value));
    }
  }

  public Predicate getNotLikePredicate(String key, String value) {
    return getNotLikePredicate(getExpression(key), value);
  }

  public Predicate getNotLikePredicate(Path<?> path, String value) {
    Expression<String> expressionPath = (Expression<String>) path;
    return criteriaBuilder.not(criteriaBuilder.like(expressionPath, value));
  }

  public Predicate getNotLikePredicate(Path<?> path, Object value) {
    Expression<String> expressionPath = (Expression<String>) path;
    Expression<String> expressionValue = criteriaBuilder.literal(value.toString());
    return criteriaBuilder.not(criteriaBuilder.like(expressionPath, expressionValue));
  }

  /** ILIKE predicates */
  public void iLike(String key, String value) {
    iLike(getExpression(key), value);
  }

  public void iLike(Path<?> path, String value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getILikePredicate(path, value));
    }
  }

  public void iLike(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getILikePredicate(path, value));
    }
  }

  public Predicate getILikePredicate(String key, String value) {
    return getILikePredicate(getExpression(key), value);
  }

  public Predicate getILikePredicate(Path<?> path, String value) {
    Expression<String> expressionPath = (Expression<String>) path;
    return criteriaBuilder.ilike(expressionPath, value);
  }

  public Predicate getILikePredicate(Path<?> path, Object value) {
    Expression<String> expressionPath = (Expression<String>) path;
    Expression<String> expressionValue = criteriaBuilder.literal(value.toString());
    return criteriaBuilder.ilike(expressionPath, expressionValue);
  }

  /** NOT ILIKE predicates */
  public void notILike(String key, String value) {
    notILike(getExpression(key), value);
  }

  public void notILike(Path<?> path, String value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotILikePredicate(path, value));
    }
  }

  public void notILike(Path<?> path, Object value) {
    if (validateValue(path, value)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getNotILikePredicate(path, value));
    }
  }

  public Predicate getNotILikePredicate(String key, String value) {
    return getNotILikePredicate(getExpression(key), value);
  }

  public Predicate getNotILikePredicate(Path<?> path, String value) {
    Expression<String> expressionPath = (Expression<String>) path;
    return criteriaBuilder.not(criteriaBuilder.ilike(expressionPath, value));
  }

  public Predicate getNotILikePredicate(Path<?> path, Object value) {
    Expression<String> expressionPath = (Expression<String>) path;
    Expression<String> expressionValue = criteriaBuilder.literal(value.toString());
    return criteriaBuilder.not(criteriaBuilder.ilike(expressionPath, expressionValue));
  }

  /** IS NULL predicates */
  public void isNull(String key) {
    isNull(getExpression(key));
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

  /** IS NOT NULL predicates */
  public void isNotNull(String key) {
    isNotNull(getExpression(key));
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

  /** Is Empty predicates */
  public void isEmpty(String key) {
    isEmpty(getExpression(key));
  }

  public void isEmpty(Path<?> path) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsEmptyPredicate(path));
  }

  private Predicate getIsEmptyPredicate(Path<?> path) {
    if (!(path.getModel() instanceof PluralAttribute)) {
      throw new IllegalStateException("isEmpty can only be used on collection attributes: " + path);
    }
    return criteriaBuilder.isEmpty((Expression<Collection<?>>) path);
  }

  /** Is Not Empty predicates */
  public void isNotEmpty(String key) {
    isNotEmpty(getExpression(key));
  }

  public void isNotEmpty(Path<?> path) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsNotEmptyPredicate(path));
  }

  private Predicate getIsNotEmptyPredicate(Path<?> path) {
    if (!(path.getModel() instanceof PluralAttribute)) {
      throw new IllegalStateException(
          "isNotEmpty can only be used on collection attributes: " + path);
    }
    return criteriaBuilder.isNotEmpty((Expression<Collection<?>>) path);
  }

  /** Between predicates */
  public void between(String key, Object from, Object to) {
    between(getExpression(key), from, to);
  }

  public void between(Path<?> key, Object from, Object to) {
    if (validateValue(key, from) && validateValue(key, to)) {
      finalPredicate = criteriaBuilder.and(finalPredicate, getBetweenPredicate(key, from, to));
    }
  }

  public Predicate getBetweenPredicate(String key, Object from, Object to) {
    return getBetweenPredicate(getExpression(key), from, to);
  }

  public Predicate getBetweenPredicate(Path<?> path, Object from, Object to) {
    return ComparisonPredicates.factory(from).getBetweenPredicate(criteriaBuilder, path, from, to);
  }

  /** Not Between predicates */
  public void notBetween(String key, Object from, Object to) {
    notBetween(getExpression(key), from, to);
  }

  public void notBetween(Path<?> key, Object from, Object to) {
    if (validateValue(key, from) && validateValue(key, to)) {
      finalPredicate =
          criteriaBuilder.and(
              finalPredicate, criteriaBuilder.not(getBetweenPredicate(key, from, to)));
    }
  }

  /** AND predicates */
  public void and(Predicate... predicates) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getAndPredicate(predicates));
  }

  public Predicate getAndPredicate(Predicate... predicates) {
    return criteriaBuilder.and(predicates);
  }

  /** OR predicates */
  public void or(Predicate... predicates) {
    finalPredicate = criteriaBuilder.or(finalPredicate, getOrPredicate(predicates));
  }

  public Predicate getOrPredicate(Predicate... predicates) {
    return criteriaBuilder.or(predicates);
  }

  /** Grouping predicates */
  public void andGroup(Predicate... predicates) {
    finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.and(predicates));
  }

  public void orGroup(Predicate... predicates) {
    finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.or(predicates));
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
    return getOrPredicate(getSearchPredicate(path, "%" + keyWord + "%"));
  }

  public Predicate getSearchPredicate(Path<?> path, String value) {
    //    return criteriaBuilder.ilike(criteriaBuilder.toString((Expression<Character>) path),
    // value);
    return criteriaBuilder.ilike(
        criteriaBuilder.lower(criteriaBuilder.toString((Expression<Character>) path)),
        value.toLowerCase());
  }

  // -------------------------------
  // Query configuration - Improved performance
  // -------------------------------

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

  public void having(Predicate... predicates) {
    if (predicates != null && predicates.length > 0) {
      query.having(predicates);
    }
  }

  public void having(List<Predicate> predicates) {
    if (!predicates.isEmpty()) {
      query.having(predicates);
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

  @SuppressWarnings("deprecation")
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
      throw new IllegalArgumentException("Failed to resolve path: " + aliasPath, e);
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

  public Predicate exists(Subquery<?> subquery) {
    return criteriaBuilder.exists(subquery);
  }

  public Predicate notExists(Subquery<?> subquery) {
    return criteriaBuilder.not(criteriaBuilder.exists(subquery));
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

      if (aliasToFullyQualifiedPathMap.containsKey(alias)) {
        throw new IllegalStateException("Alias already in use: " + alias);
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
      if (aliasToFullyQualifiedPathMap.containsKey(alias)) {
        throw new IllegalStateException("Alias already in use: " + alias);
      }
      aliasToFullyQualifiedPathMap.put(alias, fqPath.toString());
    } catch (Exception e) {
      ApplicationLogger.error("Failed to create fetch for property: " + property, e);
      throw new RuntimeException("Failed to create fetch: " + property, e);
    }
  }

  // -------------------------------
  // Date range conflict criteria - Improved with error handling
  // -------------------------------

  public void addDateTimeRangeConflictCriteria(
      DateRangeDTO dateRangeDTO, String StartDateColumn, String EndDateColumn) {
    if (dateRangeDTO == null || StartDateColumn == null || EndDateColumn == null) {
      return;
    }

    try {
      Disjunction d = new Disjunction(this);

      Conjunction startDateBetween = new Conjunction(this);
      startDateBetween.add(this.getGtPredicate(StartDateColumn, dateRangeDTO.getStart()));
      startDateBetween.add(this.getLtPredicate(StartDateColumn, dateRangeDTO.getEnd()));

      Conjunction endDateBetween = new Conjunction(this);
      endDateBetween.add(this.getGtPredicate(EndDateColumn, dateRangeDTO.getStart()));
      endDateBetween.add(this.getLtPredicate(EndDateColumn, dateRangeDTO.getEnd()));

      Conjunction startAndEnd = new Conjunction(this);
      startAndEnd.add(this.getLePredicate(StartDateColumn, dateRangeDTO.getStart()));
      startAndEnd.add(this.getGePredicate(EndDateColumn, dateRangeDTO.getEnd()));

      d.add(startDateBetween.build());
      d.add(endDateBetween.build());
      d.add(startAndEnd.build());
      this.and(d.build());
    } catch (Exception e) {
      ApplicationLogger.error("Failed to add date range conflict criteria", e);
    }
  }

  // -------------------------------
  // Validation methods - Improved with error handling
  // -------------------------------
  private boolean validateValue(Path<?> field, Object value) {
    return Objects.nonNull(value); // Skip null values for non-nullable predicates
  }

  private boolean validateValue(Path<?> field, Collection<?> value) {
    return Objects.nonNull(value)
        && !value.isEmpty(); // Skip null or empty collections for IN predicates
  }
}
