package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.Exceptions.ApplicationException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Arrays;
import java.util.List;

public class EnumDisplayValueListener {

  @PrePersist
  public void prePersist(EnumDisplayValue enumDisplayValue) {
    EnumDisplayValueDTO enumDisplayValueDTO =
        getBean(EnumDisplayValueMapper.class).map(enumDisplayValue);
    mandatoryFieldsMustBePresent(enumDisplayValueDTO);
    enumCodeMustBelongToCategory(enumDisplayValueDTO);
    categoryEnumCodeCombinationMustBeUnique(enumDisplayValueDTO);
    categoryEnumDisplayValueCombinationMustBeUnique(enumDisplayValueDTO);
  }

  public void categoryEnumCodeCombinationMustBeUnique(EnumDisplayValueDTO enumDisplayValueDTO) {
    EnumDisplayValueDTO filter = new EnumDisplayValueDTO();
    filter.setCategory(enumDisplayValueDTO.getCategory());
    filter.setEnumCode(enumDisplayValueDTO.getEnumCode());

    if (enumDisplayValueDTO.getId() != null) {
      filter.setIdsNotIn(List.of(enumDisplayValueDTO.getId()));
    }

    if (getBean(IEnumDisplayValueService.class).getCount(filter) > 0) {
      Object[] args = {enumDisplayValueDTO.getEnumCode(), enumDisplayValueDTO.getCategory()};
      throw new ApplicationException(EnumDisplayValueMessage.DUPLICATE_ENUM_CODE, args);
    }
  }

  public void categoryEnumDisplayValueCombinationMustBeUnique(
      EnumDisplayValueDTO enumDisplayValueDTO) {

    EnumDisplayValueDTO filter = new EnumDisplayValueDTO();
    filter.setCategory(enumDisplayValueDTO.getCategory());
    filter.setDisplayValue(enumDisplayValueDTO.getDisplayValue());

    if (enumDisplayValueDTO.getId() != null) {
      filter.setIdsNotIn(List.of(enumDisplayValueDTO.getId()));
    }

    if (getBean(IEnumDisplayValueService.class).getCount(filter) > 0) {
      Object[] args = {enumDisplayValueDTO.getDisplayValue(), enumDisplayValueDTO.getCategory()};
      throw new ApplicationException(EnumDisplayValueMessage.DUPLICATE_DISPLAY_VALUE, args);
    }

    displayValueShouldNotMatchOtherDefaultDisplayValue(enumDisplayValueDTO);
  }

  private void displayValueShouldNotMatchOtherDefaultDisplayValue(
      EnumDisplayValueDTO enumDisplayValueDTO) {
    Enum<?> enumObject;
    try {
      enumObject =
          Enum.valueOf(
              (Class) enumDisplayValueDTO.getCategory().getEnumClass(),
              enumDisplayValueDTO.getDisplayValue());
    } catch (Throwable t) {
      enumObject = null;
    }
    if (enumObject != null && !enumObject.toString().equals(enumDisplayValueDTO.getEnumCode())) {
      EnumDisplayValueDTO filter = new EnumDisplayValueDTO();
      filter.setCategory(enumDisplayValueDTO.getCategory());
      filter.setEnumCode(enumObject.toString());
      if (getBean(IEnumDisplayValueService.class).getCount(filter).equals(0L)) {
        Object[] args = {
          enumDisplayValueDTO.getDisplayValue(), enumDisplayValueDTO.getEnumCode(),
        };
        throw new ApplicationException(EnumDisplayValueMessage.DUPLICATE_DISPLAY_VALUE, args);
      }
    }
  }

  private void mandatoryFieldsMustBePresent(EnumDisplayValueDTO enumDisplayValueDTO) {
    if (enumDisplayValueDTO.getCategory() == null
        || enumDisplayValueDTO.getEnumCode() == null
        || enumDisplayValueDTO.getDisplayValue() == null) {
      throw new ApplicationException(EnumDisplayValueMessage.MANDATORY_FIELDS_MISSING);
    }
  }

  private void enumCodeMustBelongToCategory(EnumDisplayValueDTO enumDisplayValueDTO) {
    if (Arrays.stream(enumDisplayValueDTO.getCategory().getEnumClass().getEnumConstants())
        .map(Enum::toString)
        .noneMatch(enumDisplayValueDTO.getEnumCode()::equals)) {
      Object[] args = {enumDisplayValueDTO.getEnumCode(), enumDisplayValueDTO.getCategory()};
      throw new ApplicationException(EnumDisplayValueMessage.INVALID_ENUM_CODE, args);
    }
  }

  @PreUpdate
  public void preUpdate(EnumDisplayValue enumDisplayValue) {
    EnumDisplayValueDTO enumDisplayValueDTO =
        getBean(EnumDisplayValueMapper.class).map(enumDisplayValue);
    mandatoryFieldsMustBePresent(enumDisplayValueDTO);
    enumCodeMustBelongToCategory(enumDisplayValueDTO);
    categoryEnumCodeCombinationMustBeUnique(enumDisplayValueDTO);
    categoryEnumDisplayValueCombinationMustBeUnique(enumDisplayValueDTO);
  }
}
