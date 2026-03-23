package exambyte.application.mapper.export.mapper;

import exambyte.domain.model.user.Student;
import exambyte.infrastructure.entity.StudentEntity;

public interface StudentMapper {

    Student toDomain(StudentEntity entity);

    StudentEntity toEntity(Student student);
}
