package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.GenderEnum;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormService
    extends CommonAbstractService<ApplicationFormDTO, ApplicationForm> {
  protected ApplicationFormService(ApplicationFormMapper applicationFormMapper) {
    super(applicationFormMapper);
  }

  @Override
  protected void updateDtoBeforeCreate(ApplicationFormDTO requestDTO) {
    Map<String, Object> customFields = requestDTO.getCustomFields();
    if (customFields != null) {
      requestDTO = mapApplicationFormDTO(customFields);
      requestDTO.setCustomFields(customFields);
    }
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
          case APPLICATION_NUMBER -> dto.setApplicationNumber((String) value);

          case FIRST_NAME -> dto.setFirstName((String) value);

          case MIDDLE_NAME -> dto.setMiddleName((String) value);

          case LAST_NAME -> dto.setLastName((String) value);

          case EMAIL -> dto.setEmail((String) value);

          case PHONE_NUMBER -> dto.setPhoneNumber((String) value);

          case GENDER -> {
            String genderStr = ((String) value).toUpperCase();
            GenderEnum gender = GenderEnum.valueOf(genderStr);
            dto.setGender(gender);
          }

          case DATE_OF_BIRTH -> {
            DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String dateStr = (String) value;
            LocalDate date = null;
            try {
              date = LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (Exception e) {
              date = LocalDate.parse(dateStr, DATE_TIME_FORMATTER);
            }
            dto.setDateOfBirth(date);
          }

          case RAW_PROGRAM_NAME -> dto.setRawProgramName((String) value);

          case ADMISSION_OFFER_ID -> dto.setAdmissionOfferId(parseLong(value));

          case ADMISSION_OFFER_CODE -> dto.setAdmissionOfferCode((String) value);

          case PROGRAM_CURRICULUM_ID -> dto.setProgramCurriculumId(parseLong(value));

          case PROGRAM_CURRICULUM_CODE -> dto.setProgramCurriculumCode((String) value);

          case STARTING_PROGRAM_CYCLE_ID -> dto.setStartingProgramCycleId(parseLong(value));

          case STARTING_PROGRAM_CYCLE_CODE -> dto.setStartingProgramCycleCode((String) value);

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

  private Long parseLong(Object value) {

    if (value == null) return null;

    if (value instanceof Long l) {
      return l;
    }

    if (value instanceof Integer i) {
      return i.longValue();
    }

    if (value instanceof Double d) {
      return d.longValue(); // 51.0 → 51
    }

    if (value instanceof String s) {
      if (s.contains(".")) {
        return Double.valueOf(s).longValue();
      }
      return Long.valueOf(s);
    }

    throw new IllegalArgumentException("Cannot convert to Long: " + value);
  }
}
