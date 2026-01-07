package com.example.lazyco.core.AbstractClasses.DAO;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.OrderByDTO;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.OrderType;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.core.AbstractClasses.Filter.FilterBuilder;
import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.entities.UserManagement.UserGroup.UserGroupDTO;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@SuppressWarnings({"unchecked", "rawtypes"})
public class AbstractDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    extends PersistenceDAO<E> implements IAbstractDAO<D, E> {

  private final AbstractAction abstractAction;

  public AbstractDAO(SessionFactory sessionFactory, AbstractAction abstractAction) {
    super(sessionFactory);
    this.abstractAction = abstractAction;
  }

  public List<E> get(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters) {
    Session session = getCurrentSession();
    CriteriaBuilderWrapper criteriaBuilderWrapper =
        getCriteriaBuilderWrapper(session, filter, addEntityFilters, null);
    // order by called after adding all predicates
    criteriaBuilderWrapper.orderBy();
    Query<E> query = session.createQuery(criteriaBuilderWrapper.getQuery());
    addPaginationFilters(filter, query);
    return query.getResultList();
  }

  public List<D> get(
      D filter,
      AbstractMapper<D, E> mapper,
      BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters) {
    Session session = getCurrentSession();
    CriteriaBuilderWrapper criteriaBuilderWrapper =
        getCriteriaBuilderWrapper(session, filter, addEntityFilters, null);
    // order by called after adding all predicates
    criteriaBuilderWrapper.orderBy();

    // Calculate total count BEFORE pagination if pageSize is specified
    if (filter.getPageSize() != null) {
      filter.setTotalRecords(getCount(filter, addEntityFilters));
    }

    Query<E> query = session.createQuery(criteriaBuilderWrapper.getQuery());
    addPaginationFilters(filter, query);

    List<E> resultList = query.getResultList();

    // If no pagination was requested and no total count was set, set it to result size
    if (filter.getPageSize() == null && filter.getTotalRecords() == null) {
      filter.setTotalRecords((long) resultList.size());
    }

    return mapper.map(resultList, filter);
  }

  protected CriteriaBuilderWrapper getCriteriaBuilderWrapper(
      Session session,
      D filter,
      BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters,
      Class<?> resultClass) {
    Class<E> entityClass = (Class<E>) filter.getFilterableEntityClass();
    if (entityClass == null) {
      throw new ExceptionWrapper("Annotation @FilteredEntity does not define a valid entity type");
    }
    resultClass = resultClass == null ? entityClass : resultClass;
    HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<?> criteriaQuery = builder.createQuery(resultClass);
    Root<?> root = criteriaQuery.from(resultClass);

    CriteriaBuilderWrapper criteriaBuilderWrapper =
        new CriteriaBuilderWrapper(root, criteriaQuery, builder, filter);

    // add entity specific filters
    if (addEntityFilters != null) addEntityFilters.accept(criteriaBuilderWrapper, filter);

    // Add common filters for AbstractDTO and AbstractRBACModel
    if (AbstractRBACModel.class.isAssignableFrom(entityClass)) {
      commonAbstractDTOFilters(criteriaBuilderWrapper);
    } else {
      commonAbstractDTOUnauditedFilters(criteriaBuilderWrapper);
    }

    // Add internal field filters (fields annotated with @InternalFilterableField)
    FieldFilterUtils.addInternalFieldFilters(criteriaBuilderWrapper);

    // add Filters from filterFieldMetadata
    FilterBuilder.build(criteriaBuilderWrapper);

    // add search String filter if present
    if (filter.getSearchString() != null && !filter.getSearchString().isBlank()) {
      FieldFilterUtils.addSearchStringFilter(criteriaBuilderWrapper);
    }

    criteriaBuilderWrapper.getFinalPredicate();
    return criteriaBuilderWrapper;
  }

  protected void addPaginationFilters(AbstractDTO filter, Query query) {
    if (filter.getPageSize() != null) {
      int offset = filter.getPageOffset() != null ? filter.getPageOffset() : 0;
      query.setFirstResult(offset * filter.getPageSize()).setMaxResults(filter.getPageSize());
    }
  }

  @Override
  public Long getCount(D filter, BiConsumer<CriteriaBuilderWrapper, D> addEntityFilters) {
    Session session = getCurrentSession();
    CriteriaBuilderWrapper criteriaBuilderWrapper =
        getCriteriaBuilderWrapper(session, filter, addEntityFilters, Long.class);
    criteriaBuilderWrapper.clearOrderBy(); // Order by cannot be present in count query
    if (criteriaBuilderWrapper.isDistinct()) {
      // If distinct is applied, use countDistinct
      criteriaBuilderWrapper
          .getQuery()
          .select(
              criteriaBuilderWrapper
                  .getCriteriaBuilder()
                  .countDistinct(criteriaBuilderWrapper.getRoot()));
    } else {
      // Otherwise, use regular count
      criteriaBuilderWrapper
          .getQuery()
          .select(
              criteriaBuilderWrapper.getCriteriaBuilder().count(criteriaBuilderWrapper.getRoot()));
    }
    Long count = (Long) session.createQuery(criteriaBuilderWrapper.getQuery()).getSingleResult();
    // If count is null, return 0
    return count != null ? count : 0L;
  }

  protected void commonAbstractDTOFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    // Add filter for userGroup if the entity has userGroup field
    String userGroupFilter =
        criteriaBuilderWrapper.getFilter() != null
            ? criteriaBuilderWrapper.getFilter().getUserModifiedGroup()
            : null;
    if (userGroupFilter != null && !userGroupFilter.isEmpty())
      addRBSECFilters(criteriaBuilderWrapper, userGroupFilter);
    addRBSECFilters(criteriaBuilderWrapper);
    commonAbstractDTOUnauditedFilters(criteriaBuilderWrapper);
    // DO NOT ADD ANY RESTRICTIONS AFTER THIS POINT!!!! As we are getting the count of response
    // for the applicable filters (except from pagination) here.
  }

  protected void commonAbstractDTOUnauditedFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    if (criteriaBuilderWrapper.getFilter() != null) {
      addIdFilter(criteriaBuilderWrapper);
      addIdNotInFilter(criteriaBuilderWrapper);
      addIdInFilter(criteriaBuilderWrapper);
      applyDistinct(criteriaBuilderWrapper);
      applyOrderBy(criteriaBuilderWrapper);
    }
  }

  private void addIdFilter(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    if (criteriaBuilderWrapper.getFilter() == null
        || criteriaBuilderWrapper.getFilter().getId() == null) {
      return;
    }
    Long id = criteriaBuilderWrapper.getFilter().getId();
    criteriaBuilderWrapper.eq("id", id);
  }

  private void addIdNotInFilter(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    List notIdsInList = criteriaBuilderWrapper.getFilter().getIdsNotIn();
    if (CollectionUtils.isNotEmpty(notIdsInList)) {
      criteriaBuilderWrapper.notIn("id", notIdsInList);
    }
  }

  private void addIdInFilter(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    List inIdsList = criteriaBuilderWrapper.getFilter().getIdsIn();
    if (CollectionUtils.isNotEmpty(inIdsList)) {
      criteriaBuilderWrapper.in("id", inIdsList);
    }
  }

  private void applyDistinct(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    criteriaBuilderWrapper.setDistinct();
  }

  private void applyOrderBy(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    if (CollectionUtils.isNotEmpty(criteriaBuilderWrapper.getFilter().getOrderBy())) {
      List<OrderByDTO> orderBy = criteriaBuilderWrapper.getFilter().getOrderBy();
      for (OrderByDTO order : orderBy) {
        criteriaBuilderWrapper.orderBy(order.getOrderProperty(), order.getOrderType());
      }
    } else {
      // Default order by id desc
      criteriaBuilderWrapper.orderBy("id", OrderType.DESC);
    }
  }

  private void addRBSECFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    if (abstractAction.isBypassRBAC()) {
      ApplicationLogger.info("Bypass RBAC detected, skipping RBSEC filters");
      return;
    }

    String userGroup;
    if (abstractAction.isSystemJob()) {
      userGroup = abstractAction.getSystemJobUserGroup();
      ApplicationLogger.info(
          "System Job detected, using system job user group: " + userGroup + " to RBSEC filters");
    } else {
      UserGroupDTO userGroupDTO = abstractAction.getLoggedInUserGroup();
      userGroup = userGroupDTO != null ? userGroupDTO.getFullyQualifiedName() : null;
      ApplicationLogger.info("Logged in user's group: " + userGroup + " to RBSEC filters");
    }
    if (userGroup == null || userGroup.isEmpty()) {
      ApplicationLogger.warn("Logged in user's group is null, adding none-possible RBSEC filters");
      // In case of null user group, set a random UUID to avoid data leakage
      userGroup = UUID.randomUUID().toString();
    }
    addRBSECFilters(criteriaBuilderWrapper, userGroup);
  }

  private void addRBSECFilters(CriteriaBuilderWrapper criteriaBuilderWrapper, String groupName) {
    try {
      // Users can only see:
      // 1. Data from their exact group
      // 2. Data from their child groups (groups that start with their group + ".")
      // 3. Data from their parent groups (but only direct lineage, not siblings)

      List<String> allApplicableParentGroups = getAllApplicableParentGroups(groupName);

      // Create three predicates:
      // 1. Exact match for user's group and all parent groups in lineage
      Predicate exactGroupPredicate =
          criteriaBuilderWrapper.getInPredicate(
              AbstractRBACModel.RBAC_COLUMN, allApplicableParentGroups);

      // 2. Child groups (groups that start with user's group + ".")
      Predicate childGroupPredicate =
          criteriaBuilderWrapper.getILikePredicate(AbstractRBACModel.RBAC_COLUMN, groupName + ".%");

      // Combine with OR - user can see their lineage OR child groups
      criteriaBuilderWrapper.or(exactGroupPredicate, childGroupPredicate);
    } catch (Exception e) {
      ApplicationLogger.error(e.getMessage(), e);
    }
  }

  public static List<String> getAllApplicableParentGroups(String fullyQualifiedGroupName) {
    // for matching "." in the regex (for split) escape characters(\\) are necessary as "." in regex
    // mean "any character".
    String[] splitGroupNames = fullyQualifiedGroupName.split("\\.");
    List<String> applicableGroupNames = new ArrayList<>();
    StringBuilder groupNameBuilder = new StringBuilder();
    for (String splitGroupName : splitGroupNames) {
      groupNameBuilder.append(splitGroupName);
      applicableGroupNames.add(groupNameBuilder.toString());
      groupNameBuilder.append(".");
    }
    return applicableGroupNames;
  }
}
