package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;

public class StudentMapper {

    public Student toDomain(StudentEntity studentEntity) {
        return Student.of(studentEntity.getId(), studentEntity.getFachId(), studentEntity.getName());
    }

    public StudentEntity toEntity(Student student) {
        return new StudentEntity(student.getId(), student.uuid(), student.getName());
    }
}
