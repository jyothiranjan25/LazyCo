package com.example.lazyco.backend.core.AbstractDocClasses.DAO;

import static com.example.lazyco.backend.core.MongoCriteriaBuilder.FieldFilterUtils.addInternalFieldFilters;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractRBACModel;
import com.example.lazyco.backend.core.AbstractDocClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.MongoCriteriaBuilderWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional("mongoTransactionManager")
@SuppressWarnings({"unchecked", "rawtypes"})
public class AbstractDocDAO<D extends AbstractDTO<D>, E extends AbstractModel>
    extends PersistenceDocDAO<E> implements IAbstractDocDAO<D, E> {

  public List<E> get(D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
      Class<E> filterClass = getEntityClass(filter);
    MongoCriteriaBuilderWrapper criteriaBuilderWrapper = getCriteriaBuilderWrapper(filterClass,filter, addEntityFilters);
    Query query = criteriaBuilderWrapper.getFinalQuery();
    addPaginationFilters(filter, query);
    return getMongoTemplate().find(query, filterClass);
  }

  private MongoCriteriaBuilderWrapper getCriteriaBuilderWrapper(Class<E> filterClass,D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
    MongoCriteriaBuilderWrapper wrapper = new MongoCriteriaBuilderWrapper(filter);
    if (addEntityFilters != null) {
      addEntityFilters.accept(wrapper, filter);
    }

      if (AbstractRBACModel.class.isAssignableFrom(filterClass)) {
          commonAbstractDTOFilters(wrapper);
      } else {
          commonAbstractDTOUnauditedFilters(wrapper);
      }

    addInternalFieldFilters(wrapper);
    return wrapper;
  }

  public List<D> get(D filter, AbstractMapper<D, E> mapper, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
      Class<E> filterClass = getEntityClass(filter);
      MongoCriteriaBuilderWrapper criteriaBuilderWrapper = getCriteriaBuilderWrapper(filterClass,filter, addEntityFilters);

      // Calculate total count BEFORE pagination if pageSize is specified
      if (filter.getPageSize() != null) {
          filter.setTotalRecords(getCount(filter, addEntityFilters));
      }

      Query query = criteriaBuilderWrapper.getFinalQuery();
      addPaginationFilters(filter, query);
      List<E> resultList = getMongoTemplate().find(query, filterClass);

      // If no pagination was requested and no total count was set, set it to result size
      if (filter.getPageSize() == null && filter.getTotalRecords() == null) {
          filter.setTotalRecords((long) resultList.size());
      }
        return mapper.map(resultList);
  }

  public Long getCount(D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
      Class<E> filterClass = getEntityClass(filter);
    MongoCriteriaBuilderWrapper criteriaBuilderWrapper =
        getCriteriaBuilderWrapper(filterClass,filter, addEntityFilters);
    Query query = criteriaBuilderWrapper.getFinalQuery();
    return getMongoTemplate().count(query, filterClass);
  }

    private void addPaginationFilters(D filter, Query query) {
        if (filter.getPageSize() != null && filter.getPageOffset() != null) {
            int pageSize = filter.getPageSize();
            int pageNumber = filter.getPageOffset();
            int skip = pageNumber * pageSize; // zero-based page index
            query.skip(skip).limit(pageSize);
        }
    }

    protected void commonAbstractDTOFilters(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
        //        addRBSECFilters(criteriaBuilderWrapper);
        commonAbstractDTOUnauditedFilters(criteriaBuilderWrapper);
        // DO NOT ADD ANY RESTRICTIONS AFTER THIS POINT!!!! As we are getting the count of response
        // for the applicable filters (except from pagination) here.
    }

    protected void commonAbstractDTOUnauditedFilters(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
        if (!Objects.isNull(criteriaBuilderWrapper.getFilter())) {
            addIdFilter(criteriaBuilderWrapper);
            addIdInFilter(criteriaBuilderWrapper);
            addIdNotInFilter(criteriaBuilderWrapper);
        }
    }

    private void addIdFilter(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
        if (StringUtils.isEmpty(criteriaBuilderWrapper.getFilter().getId())) {
            return;
        }
        String id = criteriaBuilderWrapper.getFilter().getId();
        criteriaBuilderWrapper.eq("id", id);
    }

    private void addIdInFilter(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
        if (Objects.isNull(criteriaBuilderWrapper.getFilter().getIdsIn()) || criteriaBuilderWrapper.getFilter().getIdsIn().isEmpty()) {
            return;
        }
        List<String> ids = criteriaBuilderWrapper.getFilter().getIdsIn();
        Collection<String> filteredIds = ids.stream().filter(Objects::nonNull).toList();
        if (!filteredIds.isEmpty()) {
            criteriaBuilderWrapper.in("id", filteredIds);
        }
    }

    private void addIdNotInFilter(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
        if (Objects.isNull(criteriaBuilderWrapper.getFilter().getIdsNotIn()) || criteriaBuilderWrapper.getFilter().getIdsNotIn().isEmpty()) {
            return;
        }
        List<String> ids = criteriaBuilderWrapper.getFilter().getIdsNotIn();
        Collection<String> filteredIds = ids.stream().filter(Objects::nonNull).toList();
        if (!filteredIds.isEmpty()) {
            criteriaBuilderWrapper.notIn("id", filteredIds);
        }
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
}
