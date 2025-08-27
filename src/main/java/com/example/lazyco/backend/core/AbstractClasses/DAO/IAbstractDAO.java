package com.example.lazyco.backend.core.AbstractClasses.DAO;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;

import java.util.List;
import java.util.function.BiConsumer;

public interface IAbstractDAO <D extends AbstractDTO<D>, E extends AbstractModel> {

    List<E> getEntities(D filter, BiConsumer<?, D> addEntityFilters);

    List<D> getDTOs(
            D filter,
            AbstractMapper<D, E> mapper,
            BiConsumer<?, D> addEntityFilters);

    Long getCount(D filter, BiConsumer<?, D> addEntityFilters);
}
