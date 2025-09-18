package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.Exceptions.ApplicationExemption;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnumDisplayValueService extends AbstractService<EnumDisplayValueDTO, EnumDisplayValue>
    implements IEnumDisplayValueService {

  private final EnumDisplayValueMapper enumDisplayValueMapper;

  protected EnumDisplayValueService(EnumDisplayValueMapper enumDisplayValueMapper) {
    super(enumDisplayValueMapper);
    this.enumDisplayValueMapper = enumDisplayValueMapper;
  }

  protected void preCreate(EnumDisplayValueDTO dtoToCreate, EnumDisplayValue entityToCreate) {
    if (dtoToCreate.getCategory() == null || dtoToCreate.getEnumCode() == null) {
      throw new ApplicationExemption(EnumDisplayValueMessage.MANDATORY_FIELDS_MISSING);
    }
    checkDuplicateRecord(dtoToCreate);
  }

  protected void preUpdate(
      EnumDisplayValueDTO dtoToUpdate,
      EnumDisplayValue entityBeforeUpdates,
      EnumDisplayValue entityAfterUpdates) {
    if (entityAfterUpdates.getCategory() == null || entityAfterUpdates.getEnumCode() == null) {
      throw new ApplicationExemption(EnumDisplayValueMessage.MANDATORY_FIELDS_MISSING);
    }
    checkDuplicateRecord(enumDisplayValueMapper.map(entityAfterUpdates));
  }

  private void checkDuplicateRecord(EnumDisplayValueDTO dto) {
    EnumDisplayValueDTO filter = new EnumDisplayValueDTO();
    filter.setCategory(dto.getCategory());
    filter.setEnumCode(dto.getEnumCode());
    if (dto.getId() != null) {
      filter.setIdsNotIn(List.of(dto.getId()));
    }
    if (getCount(filter) > 0) {
      throw new ApplicationExemption(EnumDisplayValueMessage.DUPLICATE_ENUM_CODE);
    }
  }

  public List<EnumDisplayValueDTO> getEnumDisplayValues(EnumCategory category) {
    if (category == null) {
      throw new ExceptionWrapper("Enum category is required.");
    }
    List<EnumDisplayValueDTO> result = new ArrayList<>();
    for (Enum<?> singleEnumConstant : category.getEnumClass().getEnumConstants()) {
      EnumDisplayValueDTO singleDisplayValue = new EnumDisplayValueDTO();
      singleDisplayValue.setCategory(category);
      singleDisplayValue.setEnumCode(singleEnumConstant.toString());
      singleDisplayValue.setDisplayValue(getDisplayValue(category, singleEnumConstant.toString()));
      result.add(singleDisplayValue);
    }
    return result;
  }

  public String getDisplayValue(EnumCategory category, String enumCode) {
    EnumDisplayValueDTO filter = new EnumDisplayValueDTO();
    filter.setCategory(category);
    filter.setEnumCode(enumCode);
    EnumDisplayValueDTO result = getSingle(filter);
    if (result != null) {
      return result.getDisplayValue();
    }

    return enumCode;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Map<?, ?> getEnumCodeToDisplayValueMap(Class<? extends Enum<?>> enumClass) {
    Map<Class<? extends Enum<?>>, EnumCategory> enumClassToEnumCategoryMap =
        EnumClassToEnumCategoryCache.getEnumClassToEnumCategoryMap();
    if (!enumClassToEnumCategoryMap.containsKey(enumClass)) {
      return Set.of(enumClass.getEnumConstants()).stream()
          .collect(Collectors.toMap(Enum::name, Enum::toString));
    }
    return getEnumDisplayValues(enumClassToEnumCategoryMap.get(enumClass)).stream()
        .collect(
            Collectors.toMap(
                singleEnumDisplayValueObject ->
                    Enum.valueOf((Class) enumClass, singleEnumDisplayValueObject.getEnumCode()),
                EnumDisplayValueDTO::getDisplayValue));
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Enum<?> getEnumObject(EnumCategory category, String displayValue) {
    try {
      EnumDisplayValueDTO filter = new EnumDisplayValueDTO();
      filter.setCategory(category);
      filter.setDisplayValue(displayValue);
      EnumDisplayValueDTO result = getSingle(filter);
      String enumCode;
      if (result != null) {
        enumCode = result.getEnumCode();
      } else {
        enumCode = displayValue;
      }

      return Enum.valueOf((Class) category.getEnumClass(), enumCode);
    } catch (Throwable t) {
      return null;
    }
  }
}
