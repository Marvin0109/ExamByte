package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.user.Student;
import exambyte.persistence.entities.JDBC.StudentEntityJDBC;

public class StudentMapperJDBC {

    public Student toDomain(StudentEntityJDBC studentEntityJDBC) {
        return Student.of(studentEntityJDBC.getId(), studentEntityJDBC.getFachId(), studentEntityJDBC.getName());
    }

    public StudentEntityJDBC toEntity(Student student) {
        return new StudentEntityJDBC(student.getId(), student.uuid(), student.getName());
    }
}
