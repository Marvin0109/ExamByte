package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public static Student toDomain(StudentEntity studentEntity) {
        return Student.of(studentEntity.getId(), studentEntity.getName());
    }

    public static StudentEntity toEntity(Student student) {
        return new StudentEntity(student.getId(), student.getName());
    }
}
