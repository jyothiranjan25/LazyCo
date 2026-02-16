package com.example.lazyco.entities.ApplicationForm;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum ApplicationFormFieldEnum {
  APPLICATION_NUMBER("application_number", true, true),
  FIRST_NAME("first_name", true, true),
  MIDDLE_NAME("middle_name", false, true),
  LAST_NAME("last_name", false, true),
  GENDER("gender", false, true),
  DATE_OF_BIRTH("date_of_birth", true, true),
  EMAIL("email", true, true),
  PHONE_NUMBER("phone_number", true, true),
  APPLICATION_DATE("application_date", false, true),
  RAW_PROGRAM_NAME("raw_program_name", false, true),
  ADMISSION_OFFER_ID("admission_offer_id"),
  ADMISSION_OFFER_CODE("admission_offer_code", false, true),
  PROGRAM_CURRICULUM_ID("program_curriculum_id"),
  PROGRAM_CURRICULUM_CODE("program_curriculum_code", false, true),
  STARTING_PROGRAM_CYCLE_ID("starting_program_cycle_id"),
  STARTING_PROGRAM_CYCLE_CODE("starting_program_cycle_code", false, true);

  private final String serializedName;
  private final boolean mandatory;
  private final boolean csvExportOnly;

  ApplicationFormFieldEnum(String serializedName) {
    this(serializedName, false);
  }

  ApplicationFormFieldEnum(String serializedName, boolean mandatory) {
    this(serializedName, mandatory, false);
  }

  ApplicationFormFieldEnum(String serializedName, boolean mandatory, boolean csvExportOnly) {
    this.serializedName = serializedName;
    this.mandatory = mandatory;
    this.csvExportOnly = csvExportOnly;
  }

  public static List<String> getCSVExportFields() {
    return Arrays.stream(values())
        .filter(ApplicationFormFieldEnum::isCsvExportOnly)
        .map(ApplicationFormFieldEnum::getSerializedName)
        .toList();
  }

  public static List<String> getCSVExportMandatoryFields() {
    return Arrays.stream(values())
        .filter(ApplicationFormFieldEnum::isMandatory)
        .filter(ApplicationFormFieldEnum::isCsvExportOnly)
        .map(ApplicationFormFieldEnum::getSerializedName)
        .toList();
  }

  public static List<String> getMandatoryFields() {
    return Arrays.stream(values())
        .filter(ApplicationFormFieldEnum::isMandatory)
        .map(ApplicationFormFieldEnum::getSerializedName)
        .toList();
  }

  public static ApplicationFormFieldEnum fromSerializedName(String name) {
    return Arrays.stream(values())
        .filter(f -> f.serializedName.equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }
}
