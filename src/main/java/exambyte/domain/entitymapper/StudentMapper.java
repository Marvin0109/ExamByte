package exambyte.domain.entitymapper;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;

public interface StudentMapper {

    Student toDomain(StudentEntity studentEntity);

    StudentEntity toEntity(Student student);
}
