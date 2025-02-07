package exambyte.domain.mapper;

import exambyte.application.dto.StudentDTO;
import exambyte.domain.aggregate.user.Student;

import java.util.List;

public interface StudentDTOMapper {

    StudentDTO toDTO(Student student);

    List<StudentDTO> toStudentDTOList(List<Student> students);
}
