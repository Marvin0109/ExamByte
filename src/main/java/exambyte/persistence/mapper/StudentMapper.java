package exambyte.persistence.mapper;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.StudentEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {

    public Student toDomain(StudentEntity studentEntity) {
        return Student.of(studentEntity.getId(), studentEntity.getName());
    }

    public StudentEntity toEntity(Student student) {
        return new StudentEntity(student.getId(), student.getName());
    }
}
