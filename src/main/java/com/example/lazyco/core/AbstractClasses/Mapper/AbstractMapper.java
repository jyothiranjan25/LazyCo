package com.example.lazyco.core.AbstractClasses.Mapper;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.*;

public interface AbstractMapper<D extends AbstractDTO<D>, E extends AbstractModel> {

  @Named("map")
  D map(E entity);

  @InheritConfiguration(name = "map")
  List<D> map(List<E> entities);

  @InheritConfiguration(name = "map")
  Set<D> map(Set<E> entities);

  @InheritInverseConfiguration(name = "map")
  E map(D dto);

  @InheritInverseConfiguration(name = "map")
  List<E> mapDTOList(List<D> dtos);

  @InheritInverseConfiguration(name = "map")
  Set<E> mapDTOSet(Set<D> dtos);

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
    MapperValidator.validateAndSetNullIfFieldsAreNull(obj);
  }

  default List<D> map(List<E> entities, D filter) {
    return entities.stream().map(this::map).toList();
  }

  default Set<D> map(Set<E> entities, D filter) {
    if (filter == null) return null;
    return new HashSet<>(map(new ArrayList<>(entities), filter));
  }
}
