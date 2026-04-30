package com.example.lazyco.entities.Student;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Utils.GenderEnum;
import com.example.lazyco.entities.Admission.AdmissionDTO;
import com.example.lazyco.entities.Student.StudentCustomField.StudentCustomFieldDTO;
import com.example.lazyco.entities.Student.StudentDocument.StudentDocumentDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Student.class)
public class StudentDTO extends AbstractDTO<StudentDTO> {
  private String fullName;

  @InternalFilterableField private String firstName;

  @InternalFilterableField private String middleName;

  @InternalFilterableField private String lastName;

  @InternalFilterableField private GenderEnum gender;

  @InternalFilterableField private LocalDate dateOfBirth;

  @InternalFilterableField private String email;

  @InternalFilterableField private String phoneNumber;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "admissions.id")
  private Long admissionId;

  private List<AdmissionDTO> admissions;

  private List<StudentDocumentDTO> studentDocuments;

  private List<StudentCustomFieldDTO> studentCustomFields;
}
