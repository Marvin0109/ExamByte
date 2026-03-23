package exambyte.infrastructure.mapper;

import exambyte.domain.model.user.Student;
import exambyte.infrastructure.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student toDomain(StudentEntity entity) {
        return new Student.StudentBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    public StudentEntity toEntity(Student student) {
        return new StudentEntity.StudentEntityBuilder()
                .id(student.id())
                .name(student.getName())
                .build();
    }
}
