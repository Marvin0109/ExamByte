package exambyte.persistence.mapper.JPA;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JPA.StudentEntityJPA;
import org.springframework.stereotype.Service;

public class StudentMapperJPA {

    public Student toDomain(StudentEntityJPA studentEntityJPA) {
        return Student.of(studentEntityJPA.getId(), studentEntityJPA.getName());
    }

    public StudentEntityJPA toEntity(Student student) {
        return new StudentEntityJPA(student.getId(), student.getName());
    }
}
