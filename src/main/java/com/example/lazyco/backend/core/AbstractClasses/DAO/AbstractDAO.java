package com.example.lazyco.backend.core.AbstractClasses.DAO;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.OrderByDTO;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.backend.core.AbstractClasses.Filter.FilterBuilder;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.entities.UserManagement.AppUser.UserGroupDTO;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@SuppressWarnings({"unchecked", "rawtypes"})
public class AbstractDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    extends PersistenceDAO<E> implements IAbstractDAO<D, E> {

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
    Class<E> entityClass = getEntityClass(filter);
    resultClass = resultClass == null ? entityClass : resultClass;
    HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<?> criteriaQuery = builder.createQuery(resultClass);
    Root<?> root = criteriaQuery.from(resultClass);

    CriteriaBuilderWrapper criteriaBuilderWrapper =
        new CriteriaBuilderWrapper(root, criteriaQuery, builder, filter);

    if (addEntityFilters != null) addEntityFilters.accept(criteriaBuilderWrapper, filter);

    if (AbstractRBACModel.class.isAssignableFrom(entityClass)) {
      commonAbstractDTOFilters(criteriaBuilderWrapper);
    } else {
      commonAbstractDTOUnauditedFilters(criteriaBuilderWrapper);
    }

    // Add internal field filters (fields annotated with @InternalFilterableField)
    FieldFilterUtils.addInternalFieldFilters(criteriaBuilderWrapper);

    // add Filters from filterFieldMetadata
    FilterBuilder.build(criteriaBuilderWrapper);

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
    criteriaBuilderWrapper
        .getQuery()
        .select(
            criteriaBuilderWrapper.getCriteriaBuilder().count(criteriaBuilderWrapper.getRoot()));
    Long count = (Long) session.createQuery(criteriaBuilderWrapper.getQuery()).getSingleResult();
    // If count is null, return 0
    return count != null ? count : 0L;
  }

  public Class<E> getEntityClass(D filter) {
    // get the type parameter passed to the FilteredEntity annotation of the filter class
    try {
      return (Class<E>) filter.getClass().getAnnotation(FilteredEntity.class).type();
    } catch (Throwable t) {
      // filtered entity is not present on the dto class
      throw new ExceptionWrapper("FilteredEntity annotation is missing on " + filter.getClass());
    }
  }

  protected void commonAbstractDTOFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    // Add filter for userGroup if the entity has userGroup field
    if (criteriaBuilderWrapper.getFilter().getUserGroup() != null) {
      addRBSECFilters(criteriaBuilderWrapper, criteriaBuilderWrapper.getFilter().getUserGroup());
    }
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
    }
  }

  private void addRBSECFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    String groupName;
    try {
      // TODO: Replace with actual logged in user's group
      UserGroupDTO userGroupDTO = getBean(AbstractAction.class).loggedInUserGroup();

      String userGroup;
      if (userGroupDTO == null || userGroupDTO.getFullyQualifiedName() == null) {
        ApplicationLogger.warn("Logged in user's group is null, skipping RBSEC filters");
        userGroup = "APPLY_NONE_POSSIBLE_FILTER";
      } else {
        userGroup = userGroupDTO.getFullyQualifiedName();
      }
      addRBSECFilters(criteriaBuilderWrapper, userGroup);
    } catch (Exception e) {
      ApplicationLogger.error(e.getMessage(), e);
    }
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
