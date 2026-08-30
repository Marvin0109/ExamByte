package exambyte.application.mapper;

import exambyte.application.dto.StudentDTO;
import exambyte.domain.model.user.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentDTOMapper {

    public StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.id(),
                student.getName());
    }

    public List<StudentDTO> toStudentDTOList(List<Student> students) {
        return students.stream()
                .map(this::toDTO)
                .toList();
    }
}
