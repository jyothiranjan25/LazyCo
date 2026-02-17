package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Utils.CommonUtils;
import com.example.lazyco.core.Utils.FieldTypeEnum;
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
  protected void validateBeforeCreate(CustomFieldDTO request) {
    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(CustomFieldMessage.CUSTOM_FIELD_NAME_REQUIRED);
    } else {
      request.setKey(toKeySerialize(request.getName()));
      validateUniqueCode(request, CustomFieldMessage.CUSTOM_FIELD_KEY_ALREADY_EXISTS);
    }
    if (request.getFieldType() == null) {
      throw new ApplicationException(CustomFieldMessage.CUSTOM_FIELD_TYPE_REQUIRED);
    }
  }

  @Override
  protected void postCreate(
      CustomFieldDTO request, CustomField createdEntity, CustomFieldDTO createdDTO) {
    createdDTO.setOptions(addCustomFieldOptions(request, createdEntity));
  }

  @Override
  protected void validateBeforeUpdate(CustomFieldDTO request) {
    if (!StringUtils.isEmpty(request.getName())) {
      request.setKey(toKeySerialize(request.getName()));
      validateUniqueCode(request, CustomFieldMessage.CUSTOM_FIELD_KEY_ALREADY_EXISTS);
    }

    if (request.getFieldType() != null) {
      throw new ApplicationException(CustomFieldMessage.UPDATE_CUSTOM_FIELD_TYPE_NOT_ALLOWED);
    }
  }

  @Override
  protected void preUpdate(
      CustomFieldDTO request, CustomFieldDTO entityBeforeUpdates, CustomField entityToUpdate) {
    if (FieldTypeEnum.SELECT.equals(request.getFieldType())
        || FieldTypeEnum.MULTI_SELECT.equals(request.getFieldType())) {
      removeAssociated(entityToUpdate.getCustomFieldOptions(), request.getRemoveOptions());
    }
  }

  @Override
  protected void postUpdate(
      CustomFieldDTO request,
      CustomFieldDTO entityBeforeUpdate,
      CustomField updatedEntity,
      CustomFieldDTO updatedDTO) {
    List<CustomFieldOptionDTO> options = updatedDTO.getOptions();
    options.addAll(addCustomFieldOptions(request, updatedEntity));
  }

  private List<CustomFieldOptionDTO> addCustomFieldOptions(
      CustomFieldDTO request, CustomField entity) {
    if (!FieldTypeEnum.SELECT.equals(request.getFieldType())
        && !FieldTypeEnum.MULTI_SELECT.equals(request.getFieldType())) {
      return new ArrayList<>();
    }

    Set<CustomFieldOption> existingOptions = entity.getCustomFieldOptions();

    List<CustomFieldOptionDTO> addedOption = new ArrayList<>();
    if (request.getAddOptions() != null) {
      for (String optionValue : request.getAddOptions()) {
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
    request.setOptions(addedOption);
    return addedOption;
  }

  private String toKeySerialize(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }
    input = CommonUtils.toSnakeCase(input);

    // add _cf suffix to avoid conflict with system fields
    input = input.concat("_cf");

    return input;
  }
}
