package exambyte.service.interfaces;

import exambyte.domain.aggregate.user.Student;

import java.util.UUID;

public interface StudentService {

    Student getStudent(UUID fachId);

    void saveStudent(Student student);

    Student getStudentByName(String name);

    UUID getStudentFachId(String name);
}
