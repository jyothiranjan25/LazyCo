package com.example.lazyco.backend.core.CriteriaBuilder;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CriteriaBuilder.ComparisionPredicates.ComparisonPredicates;
import com.example.lazyco.backend.core.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.*;

@Getter
@Setter
@SuppressWarnings({"rawtypes" ,"unchecked"})
public class CriteriaBuilderWrapper {

    private Root root;

    private CriteriaQuery query;

    private HibernateCriteriaBuilder criteriaBuilder;

    private Predicate finalPredicate;

    private AbstractDTO filter;

    private Map<String, Join> joinMap;

    private Map<String, Join> fetchMap;

    private Map<String, String> aliasToFullyQualifiedPathMap;

    private Map<String, Join> fullyQualifiedPathToJoinMap;

    private Map<String, JoinType> fullyQualifiedPathToJoinTypeMap;

    private boolean isDistinct = true;

    public CriteriaBuilderWrapper(Root root, CriteriaQuery query, HibernateCriteriaBuilder criteriaBuilder, AbstractDTO filter) {
        this.root = root;
        this.query = query;
        this.criteriaBuilder = criteriaBuilder;
        this.filter = filter;
        this.finalPredicate = criteriaBuilder.conjunction();
        this.fullyQualifiedPathToJoinMap = new HashMap<>();
        this.aliasToFullyQualifiedPathMap = new HashMap<>();
        this.fullyQualifiedPathToJoinTypeMap = new HashMap<>();
    }

    public Predicate getFinalPredicate() {
        query.where(finalPredicate);
        return query.getRestriction();
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
        if (joinMap == null) {
            joinMap = new HashMap<>();
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

    /**
     * adds an equals predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for equality
     * @param value value to check for equality
     */
    public void eq(String key, Object value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getEqualPredicate(key, value));
    }

    public void eq(Path<?> path, Object value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getEqualPredicate(path, value));
    }

    public void propertyEq(String column1, String column2) {
        finalPredicate =
                criteriaBuilder.and(
                        finalPredicate, criteriaBuilder.equal(getExpression(column1), getExpression(column2)));
    }

    public void in(Path<?> path, Collection<?> value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getInPredicate(path, value));
    }

    /**
     * returns a predicate with equal operation.
     *
     * @param key key to check for equality
     * @param value value to check for equality
     * @return predicate
     */
    public Predicate getEqualPredicate(String key, Object value) {
        return criteriaBuilder.equal(getExpression(key), value);
    }

    public Predicate getEqualPredicate(Path<?> path, Object value) {
        return criteriaBuilder.equal(path, value);
    }

    /**
     * adds an equalIgnoreCase predicate in conjunction(and operation) to the final predicate using
     * passed args
     *
     * @param key key to check for equality
     * @param value value to check for equality
     */
    public void equalIgnoreCase(String key, Object value) {
        Expression lower = criteriaBuilder.lower(getExpression(key));
        finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(lower, value));
    }

    /**
     * adds a isNull predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for null
     */
    public void isNull(String key) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getIsNullPredicate(key));
    }

    public Predicate getIsNullPredicate(String key) {
        return criteriaBuilder.isNull(getExpression(key));
    }

    public void isNotNull(String key) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getIsNotNullPredicate(key));
    }

    public Predicate getIsNotNullPredicate(String key) {
        return criteriaBuilder.isNotNull(getExpression(key));
    }

    /**
     * adds a gt predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key
     * @param value value
     */
    public void gt(String key, Object value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getGtPredicate(key, value));
    }

    /**
     * this is for comparing two columns in the table
     *
     * @param column1
     * @param column2
     */
    public void greaterThan(String column1, String column2) {
        finalPredicate =
                criteriaBuilder.and(
                        finalPredicate, getGtPredicate(getExpression(column1), getExpression(column2)));
    }

    /**
     * returns a predicate with gt operation.
     *
     * @param key key
     * @param value value
     * @return predicate
     */
    public Predicate getGtPredicate(String key, Object value) {
        return getGtPredicate(getExpression(key), value);
    }

    public Predicate getGtPredicate(Path<?> path, Object value) {
        return ComparisonPredicates.factory(value).getGtPredicate(criteriaBuilder, path, value);
    }

    /** for comparing two columns in the table */
    public Predicate getGtPredicate(Path<?> path, Path<?> path2) {
        return ComparisonPredicates.factory(path).getGtPredicate(criteriaBuilder, path, path2);
    }

    /**
     * adds a lt predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for lt
     * @param value value to check for lt
     */
    public void lt(String key, Object value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getLtPredicate(key, value));
    }

    /**
     * returns a predicate with lt operation.
     *
     * @param key key to check for lt
     * @param value value to check for lt
     * @return predicate
     */
    public Predicate getLtPredicate(String key, Object value) {
        return getLtPredicate(getExpression(key), value);
    }

    public Predicate getLtPredicate(Path<?> path, Object value) {
        return ComparisonPredicates.factory(value).getLtPredicate(criteriaBuilder, path, value);
    }

    /**
     * adds a ge predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for ge
     * @param value value to check for ge
     */
    public void ge(String key, Object value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getGePredicate(key, value));
    }

    /**
     * returns a predicate with ge operation.
     *
     * @param key key to check for ge
     * @param value value to check for ge
     * @return predicate
     */
    public Predicate getGePredicate(String key, Object value) {
        return getGePredicate(getExpression(key), value);
    }

    public Predicate getGePredicate(Path<?> path, Object value) {
        return ComparisonPredicates.factory(value).getGePredicate(criteriaBuilder, path, value);
    }

    /**
     * adds a le predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for le
     * @param value value to check for le
     */
    public void le(String key, Object value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getLePredicate(key, value));
    }

    /**
     * returns a predicate with le operation.
     *
     * @param key key to check for le
     * @param value value to check for le
     * @return predicate
     */
    public Predicate getLePredicate(String key, Object value) {
        return getLePredicate(getExpression(key), value);
    }

    public Predicate getLePredicate(Path<?> path, Object value) {
        return ComparisonPredicates.factory(value).getLePredicate(criteriaBuilder, path, value);
    }

    /**
     * adds an in predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for in
     * @param value value to check for in
     */
    public void in(String key, List value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getInPredicate(key, value));
    }

    public void notIn(String key, List value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getNotInPredicate(key, value));
    }

    /**
     * returns a predicate with in operation.
     *
     * @param key key to check for in
     * @param value value to check for in
     * @return predicate
     */
    public Predicate getInPredicate(String key, List value) {
        return getInPredicate(getExpression(key), value);
    }

    public Predicate getInPredicate(Path<?> path, Collection<?> value) {
        return path.in(value);
    }

    public Predicate getNotInPredicate(String key, List value) {
        return getNotInPredicate(getExpression(key), value);
    }

    public Predicate getNotInPredicate(Path<?> path, Collection<?> value) {
        return path.in(value).not();
    }

    /**
     * adds a like predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for like
     * @param value value to check for like
     */
    public void like(String key, String value) {
        and(getLikePredicate(key, value));
    }

    /**
     * returns a predicate with in operation.
     *
     * @param key key to check for like
     * @param value value to check for like
     * @return predicate
     */
    public Predicate getLikePredicate(String key, String value) {
        return getLikePredicate(getExpression(key), value);
    }

    public Predicate getLikePredicate(Path<String> path, String value) {
        return criteriaBuilder.like(path, value);
    }

    /**
     * adds a like predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param key key to check for like
     * @param value value to check for like
     */
    public void iLike(String key, String value) {
        and(getILikePredicate(key, value));
    }

    /**
     * returns a predicate with in operation.
     *
     * @param key key to check for like
     * @param value value to check for like
     * @return predicate
     */
    public Predicate getILikePredicate(String key, String value) {
        return getILikePredicate(getExpression(key), value);
    }

    public Predicate getILikePredicate(Path<String> path, String value) {
        return criteriaBuilder.like(criteriaBuilder.lower(path), value.toLowerCase());
    }

    /**
     * adds a predicate in conjunction(and operation) to the final predicate using passed args
     *
     * @param predicates predicates to add
     */
    public void and(Predicate... predicates) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getAndPredicate(predicates));
    }

    public Predicate getAndPredicate(Predicate... predicates) {
        return criteriaBuilder.and(predicates);
    }

    public void or(Predicate... predicates) {
        and(getOrPredicate(predicates));
    }

    public Predicate getOrPredicate(Predicate... predicates) {
        return criteriaBuilder.or(predicates);
    }

    public void notEqual(String key, Object value) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getNotEqualPredicate(key, value));
    }

    public void notEqualProperty(String key, String otherKey) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getNotEqualPredicate(key, otherKey));
    }

    public Predicate getNotEqualPredicate(String key, String otherKey) {
        return criteriaBuilder.notEqual(getExpression(key), getExpression(otherKey));
    }

    public Predicate getNotEqualPredicate(String key, Object value) {
        return getNotEqualPredicate(getExpression(key), value);
    }

    public Predicate getNotEqualPredicate(Path<?> path, Object value) {
        return criteriaBuilder.notEqual(path, value);
    }

    public Predicate getBetweenPredicate(String key, Object from, Object to) {
        return getBetweenPredicate(getExpression(key), from, to);
    }

    public Predicate getBetweenPredicate(Path<?> path, Object from, Object to) {
        return ComparisonPredicates.factory(from).getBetweenPredicate(criteriaBuilder, path, from, to);
    }

    public void between(String key, Object from, Object to) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getBetweenPredicate(key, from, to));
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

    public void isEmpty(String id) {
        finalPredicate = criteriaBuilder.and(finalPredicate, getIsEmptyPredicate(id));
    }

    private Expression<Boolean> getIsEmptyPredicate(String id) {
        return criteriaBuilder.isEmpty(root.get(id));
    }
}