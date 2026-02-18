package com.example.lazyco.entities.Student;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends CommonAbstractService<StudentDTO, Student> {
  protected StudentService(StudentMapper studentMapper) {
    super(studentMapper);
  }
}
