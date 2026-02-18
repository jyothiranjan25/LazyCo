package com.example.lazyco.entities.Student;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.core.Utils.GenderEnum;
import com.example.lazyco.entities.Admission.Admission;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
    name = "student",
    comment = "table to store student information",
    indexes = {
      @Index(name = "idx_student_full_name", columnList = "first_name, middle_name, last_name"),
      @Index(name = "idx_student_gender", columnList = "gender"),
      @Index(name = "idx_student_email", columnList = "email"),
      @Index(name = "idx_student_phone_number", columnList = "phone_number")
    })
@EntityListeners(StudentListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student extends AbstractRBACModel {

  @Column(name = "first_name", nullable = false, comment = "first name of the student")
  private String firstName;

  @Column(name = "middle_name", comment = "middle name of the student")
  private String middleName;

  @Column(name = "last_name", comment = "last name of the student")
  private String lastName;

  @Column(name = "gender", comment = "gender of the student")
  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  @Column(name = "date_of_birth", comment = "date of birth of the student")
  private LocalDate dateOfBirth;

  @Column(name = "email", nullable = false, comment = "email address of the student")
  private String email;

  @Column(name = "phone_number", nullable = false, comment = "phone number of the student")
  private String phoneNumber;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
      name = "custom_fields",
      columnDefinition = "json",
      comment = "JSON column to store custom fields for the application form")
  private Map<String, Object> customFields;

  public String getFullName() {
    return mergeObject(firstName, middleName, lastName);
  }

  @OneToMany(mappedBy = "student")
  private Set<Admission> admissions;
}
