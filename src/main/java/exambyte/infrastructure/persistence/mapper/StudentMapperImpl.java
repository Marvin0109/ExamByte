package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.entitymapper.StudentMapper;
import exambyte.infrastructure.persistence.entities.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student toDomain(StudentEntity entity) {
        return new Student.StudentBuilder()
                .id(null)
                .fachId(entity.getFachId())
                .name(entity.getName())
                .build();
    }

    @Override
    public StudentEntity toEntity(Student student) {
        return new StudentEntity.StudentEntityBuilder()
                .id(null)
                .fachId(student.uuid())
                .name(student.getName())
                .build();
    }
}
