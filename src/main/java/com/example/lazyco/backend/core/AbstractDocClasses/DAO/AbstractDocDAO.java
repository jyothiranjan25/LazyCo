package com.example.lazyco.backend.core.AbstractDocClasses.DAO;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractDocClasses.Mapper.AbstractMapper;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.MongoCriteriaBuilderWrapper;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiConsumer;

@Component
@Transactional("mongoTransactionManager")
@SuppressWarnings({"unchecked", "rawtypes"})
public class AbstractDocDAO<D extends AbstractDTO<D>, E extends AbstractModel> extends PersistenceDocDAO<E> implements IAbstractDocDAO<D, E> {


    public List<E> get(D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
        MongoCriteriaBuilderWrapper criteriaBuilderWrapper = getCriteriaBuilderWrapper(filter, addEntityFilters);
        Query query = criteriaBuilderWrapper.getFinalQuery();
        return getMongoTemplate().find(query,getEntityClass(filter));
    }

    private MongoCriteriaBuilderWrapper   getCriteriaBuilderWrapper(D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
        MongoCriteriaBuilderWrapper wrapper = new MongoCriteriaBuilderWrapper(filter);
        if (addEntityFilters != null) {
            addEntityFilters.accept(wrapper, filter);
        }
        return wrapper;
    }

    public List<D> get(D filter, AbstractMapper<D, E> mapper, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
        return mapper.map(get(filter, addEntityFilters));
    }

    public Long getCount(D filter, BiConsumer<MongoCriteriaBuilderWrapper, D> addEntityFilters) {
        MongoCriteriaBuilderWrapper criteriaBuilderWrapper = getCriteriaBuilderWrapper(filter, addEntityFilters);
        Query query = criteriaBuilderWrapper.getFinalQuery();
        return getMongoTemplate().count(query, getEntityClass(filter));
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
