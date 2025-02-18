package exambyte.domain.entitymapper;

import exambyte.domain.model.aggregate.user.Student;
import exambyte.infrastructure.persistence.entities.StudentEntity;

public interface StudentMapper {

    Student toDomain(StudentEntity studentEntity);

    StudentEntity toEntity(Student student);
}
