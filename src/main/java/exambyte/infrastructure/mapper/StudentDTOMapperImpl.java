package exambyte.infrastructure.mapper;

import exambyte.application.dto.StudentDTO;
import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.mapper.StudentDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentDTOMapperImpl implements StudentDTOMapper {

    @Override
    public StudentDTO toDTO(Student student) {
        return new StudentDTO(
                null,
                student.uuid(),
                student.getName());
    }

    @Override
    public List<StudentDTO> toStudentDTOList(List<Student> students) {
        return students.stream()
                .map(this::toDTO)
                .toList();
    }
}
