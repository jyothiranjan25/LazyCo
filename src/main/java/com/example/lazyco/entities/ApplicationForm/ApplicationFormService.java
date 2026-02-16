package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.FieldParse;
import com.example.lazyco.core.Utils.GenderEnum;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormService
    extends CommonAbstractService<ApplicationFormDTO, ApplicationForm> {
  protected ApplicationFormService(ApplicationFormMapper applicationFormMapper) {
    super(applicationFormMapper);
  }

  private ApplicationFormDTO mapApplicationFormDTO(Map<String, Object> body) {
    ApplicationFormDTO dto = new ApplicationFormDTO();
    for (Map.Entry<String, Object> entry : body.entrySet()) {
      ApplicationFormFieldEnum field = ApplicationFormFieldEnum.fromSerializedName(entry.getKey());
      Object value = entry.getValue();
      if (value == null) continue;
      if (field == null) continue;

      try {
        switch (field) {
          case APPLICATION_NUMBER -> dto.setApplicationNumber(FieldParse.parseString(value));

          case FIRST_NAME -> dto.setFirstName(FieldParse.parseString(value));

          case MIDDLE_NAME -> dto.setMiddleName(FieldParse.parseString(value));

          case LAST_NAME -> dto.setLastName(FieldParse.parseString(value));

          case EMAIL -> dto.setEmail(FieldParse.parseString(value));

          case PHONE_NUMBER -> dto.setPhoneNumber(FieldParse.parseString(value));

          case GENDER -> dto.setGender(FieldParse.parseEnum(value, GenderEnum.class));

          case DATE_OF_BIRTH -> dto.setDateOfBirth(FieldParse.parseLocalDate(value));

          case RAW_PROGRAM_NAME -> dto.setRawProgramName(FieldParse.parseString(value));

          case ADMISSION_OFFER_ID -> dto.setAdmissionOfferId(FieldParse.parseLong(value));

          case ADMISSION_OFFER_CODE -> dto.setAdmissionOfferCode(FieldParse.parseString(value));

          case PROGRAM_CURRICULUM_ID -> dto.setProgramCurriculumId(FieldParse.parseLong(value));

          case PROGRAM_CURRICULUM_CODE -> dto.setProgramCurriculumCode(
              FieldParse.parseString(value));

          case STARTING_PROGRAM_CYCLE_ID -> dto.setStartingProgramCycleId(
              FieldParse.parseLong(value));

          case STARTING_PROGRAM_CYCLE_CODE -> dto.setStartingProgramCycleCode(
              FieldParse.parseString(value));

          default -> {
            // ignore export-only code fields if needed
          }
        }
      } catch (Exception e) {
        ApplicationLogger.error(e.getMessage());
      }
    }

    return dto;
  }

  public ApplicationFormDTO createCustomForm(Map<String, Object> body) {
    ApplicationFormDTO dto = mapApplicationFormDTO(body);
    dto.setCustomFields(body);
    return create(dto);
  }
}
