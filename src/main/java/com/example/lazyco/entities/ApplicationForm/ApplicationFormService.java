package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.OrConditionDTO;
import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.BatchException;
import com.example.lazyco.core.Exceptions.StandardMessageDTO;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.FieldParse;
import com.example.lazyco.core.Utils.FieldTypeEnum;
import com.example.lazyco.core.Utils.GenderEnum;
import com.example.lazyco.entities.Admission.AdmissionDTO;
import com.example.lazyco.entities.Admission.AdmissionService;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomFieldDTO;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomFieldService;
import com.example.lazyco.entities.CustomField.CustomFieldMap.CustomFieldContainer;
import com.example.lazyco.entities.CustomField.CustomFieldMap.CustomFieldValueDTO;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormService
    extends CommonAbstractService<ApplicationFormDTO, ApplicationForm> {

  private final ApplicationFormSectionCustomFieldService applicationFormSectionCustomFieldService;
  private final AdmissionService admissionService;
  private final ApplicationToAdmissionMapper applicationToAdmissionMapper;

  protected ApplicationFormService(
      ApplicationFormMapper applicationFormMapper,
      ApplicationFormSectionCustomFieldService applicationFormSectionCustomFieldService,
      AdmissionService admissionService,
      ApplicationToAdmissionMapper applicationToAdmissionMapper) {
    super(applicationFormMapper);
    this.applicationFormSectionCustomFieldService = applicationFormSectionCustomFieldService;
    this.admissionService = admissionService;
    this.applicationToAdmissionMapper = applicationToAdmissionMapper;
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

  private void validateCreateCustomFields(ApplicationFormDTO dto, StandardMessageDTO message) {

    if (dto.getAdmissionOfferId() == null) return;

    List<ApplicationFormSectionCustomFieldDTO> afCustomFields =
        applicationFormSectionCustomFieldService.get(
            new ApplicationFormSectionCustomFieldDTO() {
              {
                setAdmissionOfferId(List.of(dto.getAdmissionOfferId()));
              }
            });

    Map<String, Object> incoming = dto.getCustomFields();
    Map<String, Object> result = new HashMap<>();

    for (ApplicationFormSectionCustomFieldDTO config : afCustomFields) {

      String key = config.getCustomFieldKey();
      Object value = incoming != null ? incoming.get(key) : null;

      CustomFieldValueDTO dtoValue = validateAndBuildField(config, key, value, message);
      if (dtoValue != null) {
        result.put(config.getCustomFieldId().toString(), dtoValue);
      }
    }
    dto.setCustomFields(result);
  }

  private void validateUpdateCustomFields(ApplicationFormDTO dto, StandardMessageDTO message) {

    if (dto.getAdmissionOfferId() == null || dto.getCustomFields() == null) return;

    List<ApplicationFormSectionCustomFieldDTO> afCustomFields =
        applicationFormSectionCustomFieldService.get(
            new ApplicationFormSectionCustomFieldDTO() {
              {
                setAdmissionOfferId(List.of(dto.getAdmissionOfferId()));
              }
            });

    Map<String, ApplicationFormSectionCustomFieldDTO> configMap =
        afCustomFields.stream()
            .collect(
                Collectors.toMap(
                    ApplicationFormSectionCustomFieldDTO::getCustomFieldKey, Function.identity()));

    Map<String, Object> incoming = dto.getCustomFields();
    Map<String, Object> result = new HashMap<>();

    for (Map.Entry<String, Object> entry : incoming.entrySet()) {

      String key = entry.getKey();
      Object value = entry.getValue();

      ApplicationFormSectionCustomFieldDTO config = configMap.get(key);
      if (config == null) continue;

      CustomFieldValueDTO dtoValue = validateAndBuildField(config, key, value, message);

      if (dtoValue != null) {
        result.put(config.getCustomFieldId().toString(), dtoValue);
      }
    }

    dto.setCustomFields(result);
  }

  private CustomFieldValueDTO validateAndBuildField(
      ApplicationFormSectionCustomFieldDTO config,
      String key,
      Object value,
      StandardMessageDTO message) {

    boolean isRequired = Boolean.TRUE.equals(config.getIsRequired());
    FieldTypeEnum fieldType = config.getCustomFieldFieldType();

    if (value == null) {
      if (isRequired) {
        message.addErrorMessage("CUSTOM_FIELD", ApplicationFormMessage.CUSTOM_FIELD_REQUIRED, key);
      }
      return null;
    }

    try {
      if (!fieldType.validateField(value)) {
        message.addErrorMessage(
            "CUSTOM_FIELD", ApplicationFormMessage.CUSTOM_FIELD_INVALID_VALUE, key);
        return null;
      }
      switch (fieldType) {
        case TEXT -> FieldParse.parseString(value);
        case NUMBER -> FieldParse.parseLong(value);
        case DATE -> FieldParse.parseLocalDate(value);
        case DATETIME -> FieldParse.parseLocalDateTime(value);

        case SELECT -> {
          String str = FieldParse.parseString(value);
          boolean valid =
              config.getCustomFieldOptions().stream().anyMatch(o -> o.getOptionValue().equals(str));
          if (!valid) throw new IllegalArgumentException();
        }

        case MULTI_SELECT -> {
          List<String> values = (List<String>) FieldParse.parseList(value);
          for (String v : values) {
            boolean valid =
                config.getCustomFieldOptions().stream().anyMatch(o -> o.getOptionValue().equals(v));
            if (!valid) throw new IllegalArgumentException();
          }
        }
      }

      CustomFieldValueDTO dto = new CustomFieldValueDTO();
      dto.setKey(key);
      dto.setName(config.getCustomFieldName());
      dto.setFieldType(fieldType);
      dto.setValue(value);

      return dto;

    } catch (Exception e) {
      message.addErrorMessage(
          "CUSTOM_FIELD", ApplicationFormMessage.CUSTOM_FIELD_INVALID_VALUE, key);
      return null;
    }
  }

  public ApplicationFormDTO createCustomForm(Map<String, Object> body) {
    ApplicationFormDTO dto = mapApplicationFormDTO(body);
    dto.setCustomFields(body);
    StandardMessageDTO message = new StandardMessageDTO();
    // validate standard fields
    validateApplicationFormDTO(dto, message);
    // validate custom fields
    validateCreateCustomFields(dto, message);
    if (!message.hasErrors()) {
      dto.setSource(ApplicationFormSourceEnum.DIRECT);
      return executeCreateTransactional(dto);
    }
    dto.setMessages(message);
    throw new BatchException(dto);
  }

  @Override
  protected void validateBeforeCreate(ApplicationFormDTO request) {
    if (request.getAdmissionOfferId() == null) {
      throw new ApplicationException(ApplicationFormMessage.ADMISSION_OFFER_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getApplicationNumber())) {
      throw new ApplicationException(ApplicationFormMessage.APPLICATION_NUMBER_REQUIRED);
    }
    validateUniqueCode(request, ApplicationFormMessage.APPLICATION_NUMBER_ALREADY_EXISTS);

    if (StringUtils.isEmpty(request.getFirstName())) {
      throw new ApplicationException(ApplicationFormMessage.FIRST_NAME_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getEmail())) {
      throw new ApplicationException(ApplicationFormMessage.EMAIL_REQUIRED);
    }

    if (StringUtils.isEmpty(request.getPhoneNumber())) {
      throw new ApplicationException(ApplicationFormMessage.PHONE_NUMBER_REQUIRED);
    }

    validateUniqueApplicationForm(request);
  }

  private void validateUniqueApplicationForm(ApplicationFormDTO dto) {
    ApplicationFormDTO filter = new ApplicationFormDTO();
    if (dto.getId() != null) filter.setId(dto.getId());
    filter.setOrConditions(
        Set.of(
            new OrConditionDTO("email", dto.getEmail()),
            new OrConditionDTO("phoneNumber", dto.getPhoneNumber())));
    if (dto.getAdmissionOfferId() != null) {
      filter.setAdmissionOfferId(dto.getAdmissionOfferId());
    } else {
      filter.setAdmissionOfferCode(dto.getAdmissionOfferCode());
    }
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          ApplicationFormMessage.APPLICATION_FORM_ALREADY_EXISTS,
          new Object[] {dto.getPhoneNumber(), dto.getEmail()});
    }
  }

  public ApplicationFormDTO updateCustomForm(Map<String, Object> request) {
    ApplicationFormDTO dto = mapApplicationFormDTO(request);
    dto.setCustomFields(request);
    return executeUpdateTransactional(dto);
  }

  @Override
  protected void validateBeforeUpdate(ApplicationFormDTO request) {
    if (!StringUtils.isEmpty(request.getApplicationNumber())) {
      validateUniqueCode(request, ApplicationFormMessage.APPLICATION_NUMBER_ALREADY_EXISTS);
    }
  }

  @Override
  protected void afterMakeUpdates(
      ApplicationFormDTO request, ApplicationForm beforeUpdates, ApplicationForm afterUpdates) {
    // dont change admission offer once set
    afterUpdates.setAdmissionOffer(beforeUpdates.getAdmissionOffer());

    // validate unique application number for an admission offer
    if (!StringUtils.isEmpty(request.getEmail())
        || !StringUtils.isEmpty(request.getPhoneNumber())) {
      ApplicationFormDTO uniqueApplicationCheck = new ApplicationFormDTO();
      uniqueApplicationCheck.setId(afterUpdates.getId());
      uniqueApplicationCheck.setOrConditions(
          Set.of(
              new OrConditionDTO("email", request.getEmail()),
              new OrConditionDTO("phoneNumber", request.getPhoneNumber())));
      uniqueApplicationCheck.setAdmissionOfferId(afterUpdates.getAdmissionOffer().getId());
      validateUniqueApplicationForm(uniqueApplicationCheck);
    }

    if (request.getCustomFields() != null) {
      request.setAdmissionOfferId(afterUpdates.getAdmissionOffer().getId());
      StandardMessageDTO message = new StandardMessageDTO();
      validateUpdateCustomFields(request, message);
      request.setMessages(message);

      if (request.getCustomFields() != null) {
        // incoming customFields
        Map<String, CustomFieldValueDTO> incomingValidated =
            request.getCustomFields().entrySet().stream()
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey, entry -> (CustomFieldValueDTO) entry.getValue()));

        // existing Custom Fields before update
        Map<String, CustomFieldValueDTO> existingCustomFields =
            Optional.ofNullable(beforeUpdates.getCustomFields())
                .map(HashMap::new)
                .orElseGet(HashMap::new);

        CustomFieldContainer existingContainer = new CustomFieldContainer(existingCustomFields);
        // 🔥 Merge Logic
        for (Map.Entry<String, CustomFieldValueDTO> entry : incomingValidated.entrySet()) {
          String incomingId = entry.getKey();
          CustomFieldValueDTO incomingValue = entry.getValue();

          // 1️⃣ Try match by ID
          if (existingContainer.getById(incomingId) != null) {
            existingCustomFields.put(incomingId, incomingValue);
            continue;
          }

          // 2️⃣ Try match by KEY
          CustomFieldValueDTO existingByKey = existingContainer.getByKey(incomingValue.getKey());

          if (existingByKey != null) {
            // find the old ID for this key
            existingCustomFields.entrySet().stream()
                .filter(e -> e.getValue().getKey().equals(incomingValue.getKey()))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(existingCustomFields::remove);
          }

          // 3️⃣ Add new (or replaced)
          existingCustomFields.put(incomingId, incomingValue);
        }
        afterUpdates.setCustomFields(existingCustomFields);
      }
    }
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

  public ApplicationFormDTO enrollApplication(ApplicationFormDTO request) {
    if (request.getId() == null && request.getApplicationNumber() == null) {
      throw new ApplicationException(ApplicationFormMessage.APPLICATION_NUMBER_REQUIRED);
    }
    ApplicationFormDTO applicationForm = new ApplicationFormDTO();
    if (request.getId() != null) {
      applicationForm.setId(request.getId());
    } else {
      applicationForm.setApplicationNumber(request.getApplicationNumber());
    }
    applicationForm = getSingle(applicationForm);
    if (applicationForm.getAdmissionOfferId() == null) {
      throw new ApplicationException(ApplicationFormMessage.APPLICATION_FORM_NOT_FOUND);
    }
    if (applicationForm.getIsEnrolled()) {
      throw new ApplicationException(
          ApplicationFormMessage.APPLICATION_FORM_ALREADY_ENROLLED,
          new Object[] {applicationForm.getApplicationNumber()});
    }
    StandardMessageDTO message = new StandardMessageDTO();
    validateApplicationFormDTO(applicationForm, message);
    validateCreateCustomFields(applicationForm, message);
    if (message.hasErrors()) {
      applicationForm.setMessages(message);
      throw new BatchException(applicationForm);
    }
    AdmissionDTO admissionDTO = applicationToAdmissionMapper.map(applicationForm);
    admissionDTO = admissionService.executeCreateTransactional(admissionDTO);
    return applicationToAdmissionMapper.map(admissionDTO, applicationForm);
  }
}
