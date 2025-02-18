package exambyte.infrastructure.persistence.mapper;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.domain.entitymapper.StudentMapper;
import exambyte.infrastructure.persistence.entities.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student toDomain(StudentEntity studentEntity) {
        return new Student.StudentBuilder()
                .id(null)
                .fachId(studentEntity.getFachId())
                .name(studentEntity.getName())
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
