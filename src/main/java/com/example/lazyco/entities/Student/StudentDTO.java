package com.example.lazyco.entities.Student;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Utils.GenderEnum;
import com.example.lazyco.entities.Admission.AdmissionDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Student.class)
public class StudentDTO extends AbstractDTO<StudentDTO> {
  @InternalFilterableField private String firstName;

  @InternalFilterableField private String middleName;

  @InternalFilterableField private String lastName;

  private String fullName;

  @InternalFilterableField private GenderEnum gender;

  @InternalFilterableField private LocalDate dateOfBirth;

  @InternalFilterableField private String email;

  @InternalFilterableField private String phoneNumber;

  private Map<String, Object> customFields;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "admissions.id")
  private Long admissionId;

  private List<AdmissionDTO> admissions;
}
