package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;

public class StudentMapper {

    public static Student toDomain(StudentEntity studentEntity) {
        return new Student.StudentBuilder()
                .id(null)
                .fachId(studentEntity.getFachId())
                .name(studentEntity.getName())
                .build();
    }

    public StudentEntity toEntity(Student student) {
        return new StudentEntity.StudentEntityBuilder()
                .id(null)
                .fachId(student.uuid())
                .name(student.getName())
                .build();
    }
}
