package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModelBase;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

public interface AbstractMapper<D extends AbstractDTO<D>, E extends AbstractModelBase> extends MapBidirectionalReference<D, E> {

    D map(E entity);

    @InheritConfiguration
    List<D> map(List<E> entities);

    @InheritConfiguration
    Set<D> map(Set<E> entities);

    @InheritInverseConfiguration
    E map(D dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Named("standardDTOMapping")
    D map(D source, @MappingTarget D target);

    @Named("standardDTOMappingWithNulls")
    D mapWithNulls(D source, @MappingTarget D target);

    @Named("standardEntityMapping")
    E map(E source, @MappingTarget E target);

    @AfterMapping
    default void validateAndSetNullIfFieldsAreNull(@MappingTarget Object obj) {
        NullMappingProvider.validateAndSetNullIfFieldsAreNull(obj);
    }
}
