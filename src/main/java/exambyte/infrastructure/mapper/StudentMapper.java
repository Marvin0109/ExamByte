package exambyte.infrastructure.mapper;

import exambyte.domain.model.user.Student;
import exambyte.infrastructure.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toDomain(StudentEntity entity) {
        return new Student.StudentBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public StudentEntity toEntity(Student student) {
        return new StudentEntity.StudentEntityBuilder()
                .id(student.id())
                .name(student.getName())
                .build();
    }
}
