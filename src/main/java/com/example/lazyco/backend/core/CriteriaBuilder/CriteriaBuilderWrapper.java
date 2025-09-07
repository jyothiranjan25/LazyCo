package com.example.lazyco.backend.core.CriteriaBuilder;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CriteriaBuilder.ComparisionPredicates.ComparisonPredicates;
import com.example.lazyco.backend.core.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import jakarta.persistence.criteria.*;
import java.util.*;
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

  private Map<String, Join> joinMap = new HashMap<>();

  private Map<String, Join> fetchMap = new HashMap<>();

  private Map<String, String> aliasToFullyQualifiedPathMap = new HashMap<>();

  private Map<String, Join> fullyQualifiedPathToJoinMap = new HashMap<>();

  private Map<String, JoinType> fullyQualifiedPathToJoinTypeMap = new HashMap<>();

  private boolean isDistinct = true;

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

  private CriteriaBuilderWrapper addPredicate(Predicate predicate) {
    this.finalPredicate = criteriaBuilder.and(finalPredicate, predicate);
    return this;
  }

  // -------------------------------
  // Common predicate methods (fluent)
  // -------------------------------
  public void eq(String key, Object value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getEqualPredicate(key, value));
  }

  public void eq(Path<?> path, Object value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getEqualPredicate(path, value));
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
    Expression lower = criteriaBuilder.lower(getExpression(key));
    finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(lower, value));
  }

  public void notEqual(String key, Object value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getNotEqualPredicate(key, value));
  }

  public void notEqual(Path<?> path, Object value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getNotEqualPredicate(path, value));
  }

  public void notEqualProperty(String column1, String column2) {
    finalPredicate =
        criteriaBuilder.and(
            finalPredicate,
            criteriaBuilder.notEqual(getExpression(column1), getExpression(column2)));
  }

  public void notEqualIgnoreCase(String key, Object value) {
    Expression lower = criteriaBuilder.lower(getExpression(key));
    finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.notEqual(lower, value));
  }

  public Predicate getNotEqualPredicate(String key, Object value) {
    return getNotEqualPredicate(getExpression(key), value);
  }

  public Predicate getNotEqualPredicate(Path<?> path, Object value) {
    return criteriaBuilder.notEqual(path, value);
  }

  public void gt(String key, Object value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getGtPredicate(key, value));
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
    finalPredicate = criteriaBuilder.and(finalPredicate, getLtPredicate(key, value));
  }

  public void lesserThan(String column1, String column2) {
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
    finalPredicate = criteriaBuilder.and(finalPredicate, getGePredicate(key, value));
  }

  public void greaterThenOrEqual(String column1, String column2) {
    finalPredicate =
        criteriaBuilder.and(
            finalPredicate, getGePredicate(getExpression(column1), getExpression(column2)));
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
    finalPredicate = criteriaBuilder.and(finalPredicate, getLePredicate(key, value));
  }

  public void lesserThenOrEqual(String column1, String column2) {
    finalPredicate =
        criteriaBuilder.and(
            finalPredicate, getLePredicate(getExpression(column1), getExpression(column2)));
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
    finalPredicate = criteriaBuilder.and(finalPredicate, getInPredicate(key, value));
  }

  public void in(Path<?> path, Collection<?> value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getInPredicate(path, value));
  }

  public Predicate getInPredicate(String key, List value) {
    return getInPredicate(getExpression(key), value);
  }

  public Predicate getInPredicate(Path<?> path, Collection<?> value) {
    return path.in(value);
  }

  public void notIn(String key, List value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getNotInPredicate(key, value));
  }

  public void notIn(Path<?> path, Collection<?> value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getNotInPredicate(path, value));
  }

  public Predicate getNotInPredicate(String key, List value) {
    return getNotInPredicate(getExpression(key), value);
  }

  public Predicate getNotInPredicate(Path<?> path, Collection<?> value) {
    return path.in(value).not();
  }

  public void like(String key, String value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getLikePredicate(key, value));
  }

  public void like(Path<?> path, String value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getLikePredicate(path, value));
  }

  public Predicate getLikePredicate(String key, String value) {
    return getLikePredicate(getExpression(key), value);
  }

  public Predicate getLikePredicate(Path<?> path, String value) {
    return criteriaBuilder.like((Expression<String>) path, value);
  }

  public void iLike(String key, String value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getILikePredicate(key, value));
  }

  public void iLike(Path<?> path, String value) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getILikePredicate(path, value));
  }

  public Predicate getILikePredicate(String key, String value) {
    return getILikePredicate(getExpression(key), value);
  }

  public Predicate getILikePredicate(Path<?> path, String value) {
    return criteriaBuilder.ilike((Expression<String>) path, value);
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
    finalPredicate = criteriaBuilder.and(finalPredicate, getIsNullPredicate(path));
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
    finalPredicate = criteriaBuilder.and(finalPredicate, getBetweenPredicate(key, from, to));
  }

  public void between(Path<?> key, Object from, Object to) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getBetweenPredicate(key, from, to));
  }

  public Predicate getBetweenPredicate(String key, Object from, Object to) {
    return getBetweenPredicate(getExpression(key), from, to);
  }

  public Predicate getBetweenPredicate(Path<?> path, Object from, Object to) {
    return ComparisonPredicates.factory(from).getBetweenPredicate(criteriaBuilder, path, from, to);
  }

  public void and(Predicate... predicates) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getAndPredicate(predicates));
  }

  public Predicate getAndPredicate(Predicate... predicates) {
    return criteriaBuilder.and(predicates);
  }

  public void or(Predicate... predicates) {
    finalPredicate = criteriaBuilder.and(finalPredicate, getOrPredicate(predicates));
  }

  public Predicate getOrPredicate(Predicate... predicates) {
    return criteriaBuilder.or(predicates);
  }

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
    String[] props = property.split("\\.");
    // property must have at most 2 components. Previous alias and current property name/just
    // current
    // property name
    if (props.length == 0 || props.length > 2) {
      throw new RuntimeException("Invalid property for join: " + property);
    }

    if (props.length == 1) {
      makeJoin(root, property, alias, joinType);
    } else {
      String prevAlias = props[0];
      String currentProperty = props[1];
      Join previousJoin = joinMap.get(prevAlias);
      if (previousJoin == null) {
        throw new RuntimeException("Previous join not found for alias: " + prevAlias);
      }
      makeJoin(previousJoin, currentProperty, alias, joinType);
    }
  }

  private void makeJoin(From from, String propertyName, String alias, JoinType joinType) {
    Join join = from.join(propertyName, joinType);
    joinMap.put(alias, join);
  }

  public String getFullyQualifiedPath(String aliasPath) {
    int propertyIndex = aliasPath.lastIndexOf('.') + 1;
    String property = aliasPath.substring(propertyIndex);
    String fullyQualifiedPath;
    if (propertyIndex == 0) {
      fullyQualifiedPath = property;
    } else {
      String parentAlias = aliasPath.substring(0, propertyIndex - 1);
      fullyQualifiedPath = aliasToFullyQualifiedPathMap.get(parentAlias) + "." + property;
    }
    return fullyQualifiedPath;
  }

  public void registerAlias(String aliasPath, String alias) {
    String fullyQualifiedPath = getFullyQualifiedPath(aliasPath);
    aliasToFullyQualifiedPathMap.put(alias, fullyQualifiedPath);
  }

  public void registerAlias(String aliasPath, String alias, JoinType joinType) {
    String fullyQualifiedPath = getFullyQualifiedPath(aliasPath);
    aliasToFullyQualifiedPathMap.put(alias, fullyQualifiedPath);
    fullyQualifiedPathToJoinTypeMap.put(fullyQualifiedPath, joinType);
  }

  public void fetch(String fetchProperty) {
    root.fetch(fetchProperty);
  }

  public Path getExpression(String aliasPath) {
    String fullyQualifiedPath = getFullyQualifiedPath(aliasPath);
    return FieldFilterUtils.getPathNode(this, fullyQualifiedPath);
  }

  public Predicate getSearchCriteria(String key, String keyWord) {
    return getSearchCriteria(getExpression(key), keyWord);
  }

  public Predicate getSearchCriteria(Path<String> path, String keyWord) {
    return criteriaBuilder.or(
        getLikePredicate(path, keyWord + "%"), getLikePredicate(path, "% " + keyWord + "%"));
  }

  public void removeOrderBy() {
    query.orderBy();
  }

  public void setDistinct() {
    query.select(root).distinct(isDistinct);
  }

  public void addProjection(Expression<?>... expressions) {
    query.multiselect(expressions);
  }

  public void groupBy(List<String> fieldPaths) {
    List<Expression<?>> expressions = new ArrayList<>();
    for (String fieldPath : fieldPaths) {
      expressions.add(getExpression(fieldPath));
    }
    query.groupBy(expressions);
  }

  public void groupBy(String fieldPath) {
    groupBy(List.of(fieldPath));
  }
}
