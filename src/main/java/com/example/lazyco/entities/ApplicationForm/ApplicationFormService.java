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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
          case APPLICATION_NUMBER -> {
            dto.setApplicationNumber(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case FIRST_NAME -> {
            dto.setFirstName(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case MIDDLE_NAME -> {
            dto.setMiddleName(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case LAST_NAME -> {
            dto.setLastName(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case EMAIL -> {
            dto.setEmail(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case PHONE_NUMBER -> {
            dto.setPhoneNumber(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case APPLICATION_DATE -> {
            dto.setApplicationDate(FieldParse.parseLocalDateTime(value));
            processedKeys.add(key);
          }

          case GENDER -> {
            dto.setGender(FieldParse.parseEnum(value, GenderEnum.class));
            processedKeys.add(key);
          }

          case DATE_OF_BIRTH -> {
            dto.setDateOfBirth(FieldParse.parseLocalDate(value));
            processedKeys.add(key);
          }

          case RAW_PROGRAM_NAME -> {
            dto.setRawProgramName(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case ADMISSION_OFFER_ID -> {
            dto.setAdmissionOfferId(FieldParse.parseLong(value));
            processedKeys.add(key);
          }

          case ADMISSION_OFFER_CODE -> {
            dto.setAdmissionOfferCode(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case PROGRAM_CURRICULUM_ID -> {
            dto.setProgramCurriculumId(FieldParse.parseLong(value));
            processedKeys.add(key);
          }

          case PROGRAM_CURRICULUM_CODE -> {
            dto.setProgramCurriculumCode(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          case STARTING_PROGRAM_CYCLE_ID -> {
            dto.setStartingProgramCycleId(FieldParse.parseLong(value));
            processedKeys.add(key);
          }

          case STARTING_PROGRAM_CYCLE_CODE -> {
            dto.setStartingProgramCycleCode(FieldParse.parseString(value));
            processedKeys.add(key);
          }

          default -> {
            // leave custom fields untouched
          }
        }

      } catch (Exception e) {
        ApplicationLogger.error(e.getMessage());
      }
    }

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

    // validate custom fields
    if (dto.getAdmissionOfferId() != null && dto.getCustomFields() != null) {
      ApplicationFormSectionCustomFieldDTO sectionCustomFieldDTO =
          new ApplicationFormSectionCustomFieldDTO();
      sectionCustomFieldDTO.setAdmissionOfferId(List.of(dto.getAdmissionOfferId()));
      List<ApplicationFormSectionCustomFieldDTO> afCustomFields =
          applicationFormSectionCustomFieldService.get(sectionCustomFieldDTO);

      Map<String, Object> incomingCustomFields = dto.getCustomFields();

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

        } catch (Exception e) {

          if (isRequired) {
            message.addErrorMessage(
                "CUSTOM_FIELD", ApplicationFormMessage.CUSTOM_FIELD_INVALID_VALUE, key);
          }

          // Optional → silently remove
          incomingCustomFields.remove(key);
        }
      }
    }
  }

  public ApplicationFormDTO createCustomForm(Map<String, Object> body) {
    ApplicationFormDTO dto = mapApplicationFormDTO(body);
    dto.setCustomFields(body);
    StandardMessageDTO message = new StandardMessageDTO();
    validateApplicationFormDTO(dto, message);
    if (!message.hasErrors()) {
      return create(dto);
    }
    dto.setMessages(message);
    throw new BatchException(dto);
  }

  @Override
  protected void validateBeforeCreate(ApplicationFormDTO requestDTO) {
    if (requestDTO.getAdmissionOfferId() == null) {
      throw new ApplicationException(ApplicationFormMessage.ADMISSION_OFFER_REQUIRED);
    }
    if (requestDTO.getApplicationNumber() == null) {
      throw new ApplicationException(ApplicationFormMessage.APPLICATION_NUMBER_REQUIRED);
    }
  }
}
