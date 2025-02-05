package exambyte.service.mapper;

import exambyte.domain.aggregate.user.Student;
import exambyte.service.dto.StudentDTO;

public class StudentMapperDTO {

    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(null, student.uuid(), student.getName());
    }

    public static Student toDomain(StudentDTO dto) {
        return new Student.StudentBuilder()
                .id(null)
                .fachId(dto.fachId())
                .name(dto.name())
                .build();
    }
}
