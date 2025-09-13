package com.example.lazyco.backend.core.AbstractClasses.DAO;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

    CriteriaBuilderWrapper criteriaBuilderWrapper =
        new CriteriaBuilderWrapper(criteriaQuery.from(entityClass), criteriaQuery, builder, filter);

    if (addEntityFilters != null) addEntityFilters.accept(criteriaBuilderWrapper, filter);

    if (AbstractRBACModel.class.isAssignableFrom(entityClass)) {
      commonAbstractDTOFilters(criteriaBuilderWrapper);
    } else {
      commonAbstractDTOUnauditedFilters(criteriaBuilderWrapper);
    }

    FieldFilterUtils.addInternalFieldFilters(criteriaBuilderWrapper);

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
    return (Long) session.createQuery(criteriaBuilderWrapper.getQuery()).getSingleResult();
  }

  public Class<E> getEntityClass(D filter) {
    // get the type parameter passed to the FilteredEntity annotation of the filter class
    try {
      return (Class<E>) filter.getClass().getAnnotation(FilteredEntity.class).type();
    } catch (Throwable t) {
      // filtered entity is not present on the dto class
      throw new RuntimeException("FilteredEntity annotation is missing on " + filter.getClass());
    }
  }

  protected void commonAbstractDTOFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    //        addRBSECFilters(criteriaBuilderWrapper);
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

  //    void addRBSECFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
  //        String groupName;
  //        /** Check if a cron job is running, for which we need to disable role based user access.
  // */
  //        if (AbstractAction.getSystemJob() || AbstractAction.isBypassRBSEC()) return;
  //
  //        try {
  //            /**
  //             * logged-in users group must belong the lineage of the group of data they are
  // querying from.
  //             * Data from other lineages must be inaccessible.
  //             */
  //            UserGroupDTO aGrp = AbstractAction.getLoggedInGroup();
  //            if (aGrp == null
  //                    || aGrp.getFullyQualifiedName() == null
  //                    || aGrp.getFullyQualifiedName().isBlank()) {
  //                groupName = "blockAccessIfGroupNameIsNullForLoggedInUser";
  //            } else {
  //                groupName = aGrp.getFullyQualifiedName();
  //            }
  //
  //            // Fixed RBSEC filtering logic
  //            // Users can only see:
  //            // 1. Data from their exact group
  //            // 2. Data from their child groups (groups that start with their group + ".")
  //            // 3. Data from their parent groups (but only direct lineage, not siblings)
  //
  //            List<String> allApplicableParentGroups = getAllApplicableParentGroups(groupName);
  //
  //            // Create three predicates:
  //            // 1. Exact match for user's group and all parent groups in lineage
  //            Predicate exactGroupPredicate =
  //                    criteriaBuilderWrapper.getInPredicate("modifiedGroup",
  // allApplicableParentGroups);
  //
  //            // 2. Child groups (groups that start with user's group + ".")
  //            Predicate childGroupPredicate =
  //                    criteriaBuilderWrapper.getILikePredicate("modifiedGroup", groupName + ".%");
  //
  //            // Combine with OR - user can see their lineage OR child groups
  //            criteriaBuilderWrapper.or(exactGroupPredicate, childGroupPredicate);
  //
  //        } catch (Exception e) {
  //            ApplicationLogger.error(e.getMessage(), e);
  //        }
  //    }

  //    public static List<String> getAllApplicableParentGroups(String fullyQualifiedGroupName) {
  //        // for matching "." in the regex (for split) escape characters(\\) are necessary as "."
  // in regex
  //        // mean "any character".
  //        String[] splitGroupNames = fullyQualifiedGroupName.split("\\.");
  //        List<String> applicableGroupNames = new ArrayList<>();
  //        StringBuilder groupNameBuilder = new StringBuilder();
  //        for (String splitGroupName : splitGroupNames) {
  //            groupNameBuilder.append(splitGroupName);
  //            applicableGroupNames.add(groupNameBuilder.toString());
  //            groupNameBuilder.append(".");
  //        }
  //        return applicableGroupNames;
  //    }

  public List getAbstractFilteredResult(
      AbstractDTO filter, Class<? extends AbstractModel> entityClass) {
    Session session = getCurrentSession();
    try {
      HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<?> criteriaQuery = builder.createQuery(entityClass);
      Root<?> root = criteriaQuery.from(entityClass);
      CriteriaBuilderWrapper cbw = new CriteriaBuilderWrapper(root, criteriaQuery, builder, filter);

      if (entityClass.isAssignableFrom(AbstractRBACModel.class)) {
        commonAbstractDTOFilters(cbw);
      } else {
        commonAbstractDTOUnauditedFilters(cbw);
      }
      cbw.getFinalPredicate();
      Query<?> query = session.createQuery(cbw.getQuery());

      if (cbw.getFilter() != null) {
        addPaginationFilters(filter, query);
      }

      cbw.getFinalPredicate();
      return query.getResultList();
    } catch (Exception e) {
      ApplicationLogger.error(e.getMessage(), e);
      return List.of();
    }
  }
}
