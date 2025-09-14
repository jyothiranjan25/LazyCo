package com.example.lazyco.backend.core.AbstractClasses.Mapper;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

public interface AbstractMapper<D extends AbstractDTO<D>, E extends AbstractModel>
    extends MapBidirectionalReference<D, E> {

  @Named("map")
  D map(E entity);

  @InheritConfiguration(name = "map")
  List<D> map(List<E> entities);

  @InheritConfiguration(name = "map")
  Set<D> map(Set<E> entities);

  @InheritInverseConfiguration(name = "map")
  E map(D dto);

  // --- DTO -> DTO updates ---
  @Named("standardDTOMapping")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  D mapDTO(D source, @MappingTarget D target);

  @Named("standardDTOMappingWithNulls")
  D mapDTOWithNulls(D source, @MappingTarget D target);

  // --- Entity -> Entity updates ---
  @Named("standardEntityMapping")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  E mapEntity(E source, @MappingTarget E target);

  @Named("standardEntityMappingWithNulls") // fixed unique name
  E mapEntityWithNulls(E source, @MappingTarget E target);

  // --- DTO -> Entity updates ---
  @Named("standardDtoToEntityMapping")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  E mapDTOToEntity(D source, @MappingTarget E target);

  @Named("standardDtoToEntityMappingWithNulls")
  E mapDTOToEntityWithNulls(D source, @MappingTarget E target);

  // --- Entity -> DTO updates ---
  @Named("standardEntityToDtoMapping")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  D mapEntityToDTO(E source, @MappingTarget D target);

  @Named("standardEntityToDtoMappingWithNulls")
  D mapEntityToDTOWithNulls(E source, @MappingTarget D target);

  // Validate and set nulls after mapping
  @AfterMapping
  default void validateAndSetNullIfFieldsAreNull(@MappingTarget Object obj) {
    NullMappingProvider.validateAndSetNullIfFieldsAreNull(obj);
  }

  default List<D> map(List<E> entities, D filter) {
    return entities.stream().map(this::map).collect(Collectors.toList());
  }

  default Set<D> map(Set<E> entities, D filter) {
    if (filter == null) return null;
    return new HashSet<>(map(new ArrayList<>(entities), filter));
  }
}
