package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.BatchException;
import com.example.lazyco.core.Exceptions.StandardMessageDTO;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.FieldParse;
import com.example.lazyco.core.Utils.FieldTypeEnum;
import com.example.lazyco.core.Utils.GenderEnum;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomFieldDTO;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomFieldService;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormService
    extends CommonAbstractService<ApplicationFormDTO, ApplicationForm> {

  private final ApplicationFormSectionCustomFieldService applicationFormSectionCustomFieldService;

  protected ApplicationFormService(
      ApplicationFormMapper applicationFormMapper,
      ApplicationFormSectionCustomFieldService applicationFormSectionCustomFieldService) {
    super(applicationFormMapper);
    this.applicationFormSectionCustomFieldService = applicationFormSectionCustomFieldService;
  }

  private ApplicationFormDTO mapApplicationFormDTO(Map<String, Object> body) {

    ApplicationFormDTO dto = new ApplicationFormDTO();

    Set<String> processedKeys = new HashSet<>();
    for (Map.Entry<String, Object> entry : body.entrySet()) {

      String key = entry.getKey();
      Object value = entry.getValue();

      if (value == null) continue;

      ApplicationFormFieldEnum field = ApplicationFormFieldEnum.fromSerializedName(key);

      if (field == null) continue;

      try {
        switch (field) {
          case ID -> {
            processedKeys.add(key);
            dto.setId(FieldParse.parseLong(value));
          }

          case APPLICATION_NUMBER -> {
            processedKeys.add(key);
            dto.setApplicationNumber(FieldParse.parseString(value));
          }

          case FIRST_NAME -> {
            processedKeys.add(key);
            dto.setFirstName(FieldParse.parseString(value));
          }

          case MIDDLE_NAME -> {
            processedKeys.add(key);
            dto.setMiddleName(FieldParse.parseString(value));
          }

          case LAST_NAME -> {
            processedKeys.add(key);
            dto.setLastName(FieldParse.parseString(value));
          }

          case EMAIL -> {
            processedKeys.add(key);
            dto.setEmail(FieldParse.parseString(value));
          }

          case PHONE_NUMBER -> {
            processedKeys.add(key);
            dto.setPhoneNumber(FieldParse.parseString(value));
          }

          case APPLICATION_DATE -> {
            processedKeys.add(key);
            dto.setApplicationDate(FieldParse.parseLocalDateTime(value));
          }

          case GENDER -> {
            processedKeys.add(key);
            dto.setGender(FieldParse.parseEnum(value, GenderEnum.class));
          }

          case DATE_OF_BIRTH -> {
            processedKeys.add(key);
            dto.setDateOfBirth(FieldParse.parseLocalDate(value));
          }

          case RAW_PROGRAM_NAME -> {
            processedKeys.add(key);
            dto.setRawProgramName(FieldParse.parseString(value));
          }

          case ADMISSION_OFFER_ID -> {
            processedKeys.add(key);
            dto.setAdmissionOfferId(FieldParse.parseLong(value));
          }

          case ADMISSION_OFFER_CODE -> {
            processedKeys.add(key);
            dto.setAdmissionOfferCode(FieldParse.parseString(value));
          }

          case PROGRAM_CURRICULUM_ID -> {
            processedKeys.add(key);
            dto.setProgramCurriculumId(FieldParse.parseLong(value));
          }

          case PROGRAM_CURRICULUM_CODE -> {
            processedKeys.add(key);
            dto.setProgramCurriculumCode(FieldParse.parseString(value));
          }

          case STARTING_PROGRAM_CYCLE_ID -> {
            processedKeys.add(key);
            dto.setStartingProgramCycleId(FieldParse.parseLong(value));
          }

          case STARTING_PROGRAM_CYCLE_CODE -> {
            processedKeys.add(key);
            dto.setStartingProgramCycleCode(FieldParse.parseString(value));
          }

          default -> {
            // leave custom fields untouched
          }
        }
      } catch (Exception e) {
        ApplicationLogger.error(e.getMessage());
      }
    }

    // Remove all processed keys from the original map to isolate custom fields
    processedKeys.forEach(body::remove);

    return dto;
  }

  private void validateApplicationFormDTO(ApplicationFormDTO dto, StandardMessageDTO message) {
    if (dto.getAdmissionOfferId() == null && dto.getAdmissionOfferCode() == null) {
      message.addErrorMessage(ApplicationFormMessage.ADMISSION_OFFER_REQUIRED);
    }

    if (dto.getApplicationNumber() == null) {
      message.addErrorMessage(ApplicationFormMessage.APPLICATION_NUMBER_REQUIRED);
    }

    if (dto.getFirstName() == null) {
      message.addErrorMessage(ApplicationFormMessage.FIRST_NAME_REQUIRED);
    }

    if (dto.getEmail() == null) {
      message.addErrorMessage(ApplicationFormMessage.EMAIL_REQUIRED);
    } else {
      FieldTypeEnum field = FieldTypeEnum.EMAIL;
      if (!field.validateField(dto.getEmail())) {
        message.addErrorMessage(ApplicationFormMessage.EMAIL_INVALID);
      }
    }

    if (dto.getPhoneNumber() == null) {
      message.addErrorMessage(ApplicationFormMessage.PHONE_NUMBER_REQUIRED);
    } else {
      FieldTypeEnum field = FieldTypeEnum.PHONE;
      if (!field.validateField(dto.getPhoneNumber())) {
        message.addErrorMessage(ApplicationFormMessage.PHONE_NUMBER_INVALID);
      }
    }
  }

  private void validateCustomFields(ApplicationFormDTO dto, StandardMessageDTO message) {
    if (dto.getAdmissionOfferId() != null && dto.getCustomFields() != null) {
      ApplicationFormSectionCustomFieldDTO sectionCustomFieldDTO =
          new ApplicationFormSectionCustomFieldDTO();
      sectionCustomFieldDTO.setAdmissionOfferId(List.of(dto.getAdmissionOfferId()));
      List<ApplicationFormSectionCustomFieldDTO> afCustomFields =
          applicationFormSectionCustomFieldService.get(sectionCustomFieldDTO);

      Map<String, Object> incomingCustomFields = dto.getCustomFields();

      List<String> validFields = new ArrayList<>();
      for (ApplicationFormSectionCustomFieldDTO customField : afCustomFields) {

        String key = customField.getCustomFieldKey();
        Object value = incomingCustomFields.get(key);

        boolean isRequired = Boolean.TRUE.equals(customField.getIsRequired());

        // ------------------------------
        // 1️⃣ Missing value
        // ------------------------------
        if (value == null) {

          if (isRequired) {
            message.addErrorMessage(
                "CUSTOM_FIELD", ApplicationFormMessage.CUSTOM_FIELD_REQUIRED, key);
          }

          // Remove null or missing key
          incomingCustomFields.remove(key);
          continue;
        }

        try {

          FieldTypeEnum fieldType = customField.getCustomFieldFieldType();

          // ------------------------------
          // 2️⃣ Basic Type Validation
          // ------------------------------
          if (!fieldType.validateField(value)) {

            if (isRequired) {
              message.addErrorMessage(
                  "CUSTOM_FIELD", ApplicationFormMessage.CUSTOM_FIELD_INVALID_VALUE, key);
            }

            incomingCustomFields.remove(key);
            continue;
          }

          // ------------------------------
          // 3️⃣ Type-Specific Parsing & Validation
          // ------------------------------
          switch (fieldType) {
            case TEXT -> FieldParse.parseString(value);

            case NUMBER -> FieldParse.parseLong(value);

            case DATE -> FieldParse.parseLocalDate(value);

            case DATETIME -> FieldParse.parseLocalDateTime(value);

            case SELECT -> {
              String strValue = FieldParse.parseString(value);

              boolean valid =
                  customField.getCustomFieldOptions().stream()
                      .anyMatch(option -> option.getOptionValue().equals(strValue));

              if (!valid) throw new IllegalArgumentException();
            }

            case MULTI_SELECT -> {
              @SuppressWarnings("unchecked")
              List<String> values = (List<String>) FieldParse.parseList(value);

              for (String val : values) {
                boolean valid =
                    customField.getCustomFieldOptions().stream()
                        .anyMatch(option -> option.getOptionValue().equals(val));

                if (!valid) throw new IllegalArgumentException();
              }
            }
          }
          validFields.add(key);
        } catch (Exception e) {

          if (isRequired) {
            message.addErrorMessage(
                "CUSTOM_FIELD", ApplicationFormMessage.CUSTOM_FIELD_INVALID_VALUE, key);
          }

          // Optional → silently remove
          incomingCustomFields.remove(key);
        }
      }
      // Remove any incoming fields that are not defined in the system for this admission offer
      incomingCustomFields.keySet().removeIf(key -> !validFields.contains(key));
    }
  }

  public ApplicationFormDTO createCustomForm(Map<String, Object> body) {
    ApplicationFormDTO dto = mapApplicationFormDTO(body);
    dto.setCustomFields(body);
    StandardMessageDTO message = new StandardMessageDTO();
    // validate standard fields
    validateApplicationFormDTO(dto, message);
    // validate custom fields
    validateCustomFields(dto, message);
    if (!message.hasErrors()) {
      dto.setSource(ApplicationFormSourceEnum.DIRECT);
      return create(dto);
    }
    dto.setMessages(message);
    throw new BatchException(dto);
  }

  @Override
  protected void validateBeforeCreate(ApplicationFormDTO request) {
    if (request.getAdmissionOfferId() == null) {
      throw new ApplicationException(ApplicationFormMessage.ADMISSION_OFFER_REQUIRED);
    }
    if (request.getApplicationNumber() == null) {
      throw new ApplicationException(ApplicationFormMessage.APPLICATION_NUMBER_REQUIRED);
    }
  }

  public ApplicationFormDTO updateCustomForm(Map<String, Object> request) {
    ApplicationFormDTO dto = mapApplicationFormDTO(request);
    dto.setCustomFields(request);
    return update(dto);
  }

  @Override
  protected void afterMakeUpdates(
      ApplicationFormDTO request, ApplicationForm beforeUpdates, ApplicationForm afterUpdates) {
    // dont change admission offer once set
    afterUpdates.setAdmissionOffer(beforeUpdates.getAdmissionOffer());

    Map<String, Object> existingCustomFields =
        Optional.ofNullable(beforeUpdates.getCustomFields())
            .map(HashMap::new)
            .orElseGet(HashMap::new);
    if (request.getCustomFields() != null) {
      // validate custom fields
      StandardMessageDTO message = new StandardMessageDTO();
      request.setAdmissionOfferId(beforeUpdates.getAdmissionOffer().getId());
      validateCustomFields(request, message);
      request.setMessages(message);
      Map<String, Object> incomingCustomFields = request.getCustomFields();
      for (Map.Entry<String, Object> entry : incomingCustomFields.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        existingCustomFields.put(key, value);
      }
    }
    afterUpdates.setCustomFields(existingCustomFields);
  }

  @Override
  protected ApplicationFormDTO modifyUpdateResult(
      ApplicationFormDTO request, ApplicationFormDTO updatedDTO) {
    updatedDTO.setMessages(request.getMessages());
    return updatedDTO;
  }

  public ApplicationFormDTO deleteCustomForm(ApplicationFormDTO request) {
    return super.delete(request);
  }
}
