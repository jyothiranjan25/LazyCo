package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Utils.FieldInputType;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOption;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOptionDTO;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOptionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomFieldService extends CommonAbstractService<CustomFieldDTO, CustomField> {

  private final CustomFieldOptionService customFieldOptionService;

  protected CustomFieldService(
      CustomFieldMapper customFieldMapper, CustomFieldOptionService customFieldOptionService) {
    super(customFieldMapper);
    this.customFieldOptionService = customFieldOptionService;
  }

  @Override
  protected void validateBeforeCreate(CustomFieldDTO requestDTO) {
    if (StringUtils.isEmpty(requestDTO.getName())) {
      throw new ApplicationException(CustomFieldMessage.CUSTOM_FIELD_NAME_REQUIRED);
    }
  }

  @Override
  protected void postCreate(
      CustomFieldDTO requestDTO, CustomField createdEntity, CustomFieldDTO createdDTO) {
    createdDTO.setOptions(addCustomFieldOptions(requestDTO, createdEntity));
  }

  @Override
  protected void preUpdate(
      CustomFieldDTO requestDTO, CustomFieldDTO entityBeforeUpdates, CustomField entityToUpdate) {
    if (FieldInputType.SELECT.equals(requestDTO.getFieldType())
        || FieldInputType.MULTI_SELECT.equals(requestDTO.getFieldType())) {
      removeAssociated(entityToUpdate.getCustomFieldOptions(), requestDTO.getRemoveOptions());
    }
  }

  @Override
  protected void postUpdate(
      CustomFieldDTO requestDTO,
      CustomFieldDTO entityBeforeUpdate,
      CustomField updatedEntity,
      CustomFieldDTO updatedDTO) {
    List<CustomFieldOptionDTO> options = updatedDTO.getOptions();
    options.addAll(addCustomFieldOptions(requestDTO, updatedEntity));
  }

  private List<CustomFieldOptionDTO> addCustomFieldOptions(
      CustomFieldDTO requestDTO, CustomField entity) {
    if (!FieldInputType.SELECT.equals(requestDTO.getFieldType())
        && !FieldInputType.MULTI_SELECT.equals(requestDTO.getFieldType())) {
      return new ArrayList<>();
    }

    Set<CustomFieldOption> existingOptions = entity.getCustomFieldOptions();

    List<CustomFieldOptionDTO> addedOption = new ArrayList<>();
    if (requestDTO.getAddOptions() != null) {
      for (String optionValue : requestDTO.getAddOptions()) {
        if (optionValue == null || optionValue.trim().isEmpty()) {
          continue;
        }
        String trimmedValue = optionValue.trim();

        if (existingOptions.stream()
            .anyMatch(opt -> opt.getOptionValue().equalsIgnoreCase(trimmedValue))) {
          continue;
        }

        CustomFieldOptionDTO newOption = new CustomFieldOptionDTO();
        newOption.setOptionValue(trimmedValue);
        newOption.setCustomFieldId(entity.getId());
        newOption = customFieldOptionService.executeCreateTransactional(newOption);
        addedOption.add(newOption);
      }
    }
    requestDTO.setOptions(addedOption);
    return addedOption;
  }
}
